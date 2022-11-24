package model.classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.util.HashMap;

import model.haircutDecorator.*;
import model.singletons.DBConnectionSingleton;

public class Appointment {
	private int id;
	private LocalDateTime timeOfAppointment;
	private iHaircutStyle choosenStyle;
	private HashMap<Product, Integer> productsCart;

	public Appointment(int id, LocalDateTime timeOfAppointment, iHaircutStyle choosenStyle) {
		this.id = id;
		this.timeOfAppointment = timeOfAppointment;
		this.choosenStyle = choosenStyle;
		this.productsCart = new HashMap<Product, Integer>();
	}
	
	public Appointment(LocalDateTime timeOfAppointment, iHaircutStyle choosenStyle) {
		this.timeOfAppointment = timeOfAppointment;
		this.choosenStyle = choosenStyle;
		this.productsCart = new HashMap<Product, Integer>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public iHaircutStyle getChoosenStyle() {
		return choosenStyle;
	}

	public void setChoosenStyle(iHaircutStyle choosenStyle) {
		this.choosenStyle = choosenStyle;
	}

	public LocalDateTime getTimeOfAppointment() {
		return timeOfAppointment;
	}

	public IsraelDayOfWeek getDayOfWeek() {
		return IsraelDayOfWeek.fromJavaDayOfWeek(timeOfAppointment.getDayOfWeek());
	}

	public LocalDateTime getTimeOfEndAppointment() {
		return timeOfAppointment.plusMinutes(choosenStyle.getTotalLenght());
	}

	public void setTimeOfAppointment(LocalDateTime timeOfAppointment) {
		this.timeOfAppointment = timeOfAppointment;
	}

	public HashMap<Product, Integer> getProductsCart() {
		return productsCart;
	}
	
	public void setProductsCart(HashMap<Product, Integer> products) {
		this.productsCart = products;
	}

	public void initProductInCart(Product prod, int count) throws SQLException {
		productsCart.put(prod, count);
	}
	
	public void addProductToCart(Product prod, int count) throws SQLException {
		if(productsCart.put(prod, count) != null) {
			updateProductInCart(prod, count);
			return;
		}
		String query = "INSERT INTO shoppingcart VALUES (?, ?, ?);";
		PreparedStatement pst = DBConnectionSingleton.getInstance().getConnection().prepareStatement(query);
		pst.setInt(1, this.getId());
		pst.setInt(2, prod.getId());
		pst.setInt(3, count);
		if (pst.executeUpdate() != 1)
			throw new SQLException();
	}
	
	public void updateProductInCart(Product prod, int count) throws SQLException {
		if(productsCart.put(prod, count) == null) {
			addProductToCart(prod, count);
			return;
		}
		String query = "UPDATE shoppingcart SET count = ? WHERE appID = ? AND prodID = ?;";
		PreparedStatement pst = DBConnectionSingleton.getInstance().getConnection().prepareStatement(query);
		pst.setInt(1, count);
		pst.setInt(2, this.getId());
		pst.setInt(3, prod.getId());
		if (pst.executeUpdate() != 1)
			throw new SQLException();
	}
	
	public void removeProductFromCart(Product prod) throws SQLException {
		productsCart.remove(prod);
		String query = "DELETE FROM shoppingcart WHERE appID = ? AND prodID = ?;";
		PreparedStatement pst = DBConnectionSingleton.getInstance().getConnection().prepareStatement(query);
		pst.setInt(1, this.getId());
		pst.setInt(2, prod.getId());
		if (pst.executeUpdate() != 1)
			throw new SQLException();
	}
	
	public int sumProductsCost() {
		int sum = 0;
		for (HashMap.Entry<Product, Integer> prod : productsCart.entrySet())
			sum += prod.getKey().getPrice() * prod.getValue();
		
		return sum;
	}
	
}
