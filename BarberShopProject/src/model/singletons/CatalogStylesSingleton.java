package model.singletons;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import model.classes.StyleType;
import model.classes.StyleType.eStyleTypes;
import mvc_controller.iModelListenable;

import java.sql.*;

public class CatalogStylesSingleton {
	
	public  String manAndBeard;
	public  String edgesAndColor;
	public  String edgesAndShades;
	public  String edgesAndStraightening;
	public  String colorAndStraightening;
	public  String shadesAndStraightening;
	
	private static CatalogStylesSingleton _instance = null;
	private static HashMap<eStyleTypes, StyleType> typesList;
	private static Connection conn;
	private ArrayList<iModelListenable> allListeners;

	
	private CatalogStylesSingleton() throws SQLException {
		conn = DBConnectionSingleton.getInstance().getConnection();
		typesList = new HashMap<eStyleTypes, StyleType>();		
		typesList.put(eStyleTypes.eChild, 		new StyleType(eStyleTypes.eChild));
		typesList.put(eStyleTypes.eMan, 		new StyleType(eStyleTypes.eMan));
		typesList.put(eStyleTypes.eBeard, 		new StyleType(eStyleTypes.eBeard));
		typesList.put(eStyleTypes.eEdges, 		new StyleType(eStyleTypes.eEdges));
		typesList.put(eStyleTypes.eColor, 		new StyleType(eStyleTypes.eColor));
		typesList.put(eStyleTypes.eShades, 		new StyleType(eStyleTypes.eShades));
		typesList.put(eStyleTypes.eStraight, 	new StyleType(eStyleTypes.eStraight));
		readAllStylesDB();
		manAndBeard = 				getStyle(eStyleTypes.eMan).getName()
									+  " + " +
									getStyle(eStyleTypes.eBeard).getName();
		
		edgesAndColor = 			getStyle(eStyleTypes.eEdges).getName()
									+  " + " +
									getStyle(eStyleTypes.eColor).getName();
		
		edgesAndShades = 			getStyle(eStyleTypes.eEdges).getName()
									+  " + " +
									getStyle(eStyleTypes.eShades).getName();
		
		edgesAndStraightening = 	getStyle(eStyleTypes.eEdges).getName()
									+  " + " +
									getStyle(eStyleTypes.eStraight).getName();
		
		colorAndStraightening = 	getStyle(eStyleTypes.eColor).getName()
									+  " + " +
									getStyle(eStyleTypes.eStraight).getName();
		
		shadesAndStraightening= 	getStyle(eStyleTypes.eShades).getName()
									+  " + " +
									getStyle(eStyleTypes.eStraight).getName();
	}
	
	public static CatalogStylesSingleton getInstance() throws SQLException {
		if(_instance == null)
			_instance = new CatalogStylesSingleton();
		return _instance;
	}
	
	public void registerListener(iModelListenable l) {
		if (allListeners == null) {
			allListeners = new ArrayList<iModelListenable>();
		}
		if(!allListeners.contains(l))
			allListeners.add(l);
	}

	public void removeListener(iModelListenable l) {
		if (allListeners == null) {
			return;
		}
		allListeners.remove(l);
	}
	
	public StyleType getStyle(eStyleTypes type) {
		return typesList.get(type);
	}

	private void readAllStylesDB() throws SQLException {
		for (HashMap.Entry<eStyleTypes, StyleType> entry : typesList.entrySet()) {
			readStyleFromDB(entry.getValue());		
		}
	}
	
	private void readStyleFromDB(StyleType type) throws SQLException {
		String query = "SELECT * FROM styles WHERE tag = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, type.getType().toString());
		ResultSet rs = pst.executeQuery();
		
		while (rs.next()) {			
			type.setName(rs.getString("name"));
			if(rs.wasNull())
				type.setName("");
			type.setLenght(rs.getInt("lengthInMinutes"));
			type.setPrice(rs.getInt("price"));
		}
	}
		
	public void updateStyleInDB(eStyleTypes type, int lenght, int price) throws SQLException {
		String query = "UPDATE styles SET lengthInMinutes = ?, price = ? WHERE tag = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, lenght);
		pst.setInt(2, price);
		pst.setString(3, type.toString());
		
		int numOfRowsAffected = pst.executeUpdate();
		if(numOfRowsAffected != 1)
			throw new SQLException();
		readAllStylesDB();
	}

	public List<String> getAllStylesOptionsStringList(){
		String man = getStyle(eStyleTypes.eMan).getName();
		String child = getStyle(eStyleTypes.eChild).getName();
		String edges = getStyle(eStyleTypes.eEdges).getName();
		String color = getStyle(eStyleTypes.eColor).getName();
		String shades = getStyle(eStyleTypes.eShades).getName();
		String straight = getStyle(eStyleTypes.eStraight).getName();		
	
		List<String> allStyleOptions = Arrays.asList
									(man, child, edges, color, shades, straight,
									manAndBeard, edgesAndColor, edgesAndShades , 
									edgesAndStraightening , colorAndStraightening,
									shadesAndStraightening);
		return allStyleOptions;
	}
	
	public ArrayList<StyleType> getAllStyles(){
		ArrayList<StyleType> styleList = new ArrayList<StyleType>();
		styleList.addAll(typesList.values());
		return styleList;
	}
	
}