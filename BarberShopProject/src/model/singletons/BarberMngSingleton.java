package model.singletons;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

import model.classes.*;
import mvc_controller.iModelListenable;

public class BarberMngSingleton {

	private static BarberMngSingleton _instance;
	private HashSet<Product> allProducts;
	private static Connection conn;
	private ArrayList<iModelListenable> allListeners;
	
	private BarberMngSingleton() throws SQLException {
		allProducts = new HashSet<Product>();
		conn = DBConnectionSingleton.getInstance().getConnection();
		readAllProductsFromDB();
		ActivityHoursSingleton.getInstance();
	}

	public static BarberMngSingleton getInstance() throws SQLException {
		if (_instance == null) {
			_instance = new BarberMngSingleton();
		}
		return _instance;
	}

	public void registerListener(iModelListenable l) {
		if (allListeners == null) {
			allListeners = new ArrayList<iModelListenable>();
		}
		allListeners.add(l);
	}
	
	public void removeListener(iModelListenable l) {
		if (allListeners == null) {
			return;
		}
		allListeners.remove(l);
	}
	
	public void readAllProductsFromDB() throws SQLException {
		String query = "SELECT * FROM products";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		Product theProduct = null;
		while (rs.next()) {
			int prodID = rs.getInt("ID");
			String name = rs.getString("name");
			int price = rs.getInt("price");
			theProduct = new Product(prodID, name, price);
			allProducts.add(theProduct);
		}
	}

	public void updateProductDetailsInDB(Product prod) throws SQLException {
		String query = "UPDATE products SET name = ?, price = ? WHERE ID = ?;";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, prod.getName());
		pst.setInt(2, prod.getPrice());
		pst.setInt(3, prod.getId());
		if (pst.executeUpdate() != 1)
			throw new SQLException();
	}

	public void removeProductFromDB(Product prod) throws SQLException {
		String query = "DELETE FROM products WHERE ID = ?;";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, prod.getId());
		if (pst.executeUpdate() != 1)
			throw new SQLException();
		allProducts.clear();
		readAllProductsFromDB();
	}

	public void addProductToDB(Product newProd) throws SQLException {
		String query = "INSERT INTO products (name, price) VALUES (?,?);";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, newProd.getName());
		pst.setInt(2, newProd.getPrice());
		if (pst.executeUpdate() != 1)
			throw new SQLException();
		readProductIdFromDB(newProd);
		allProducts.add(newProd);
	}

	public void readProductIdFromDB(Product prod) throws SQLException {
		String query = "SELECT ID FROM products WHERE name = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, prod.getName());
		ResultSet rs = pst.executeQuery();
		while (rs.next())
			prod.setId(rs.getInt("ID"));
	}

	public HashSet<Product> getAllProducts() {
		return allProducts;
	}

	public Product getProductByID(int id) {
		for (Product p : allProducts) {
			if (p.getId() == id)
				return p;
		}
		return null;
	}
}