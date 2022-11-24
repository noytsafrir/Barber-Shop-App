package model.singletons;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import model.classes.*;
import model.exceptions.*;
import model.haircutDecorator.*;
import mvc_controller.iModelListenable;

public class UserSingleton {
	private static UserSingleton _instance = null;
	private Customer theCustomer;
	private boolean isLoggedIn;
	private static Connection conn;
	private ArrayList<iModelListenable> allListeners;


	// A private Constructor prevents any other class from create another object
	// from this class
	private UserSingleton() throws SQLException {
		conn = DBConnectionSingleton.getInstance().getConnection();
		this.theCustomer = null;
		this.isLoggedIn = false;
	}

	/* Static 'instance' method */
	public static UserSingleton getInstance() throws SQLException {
		if (_instance == null)
			_instance = new UserSingleton();
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
	
	public void logIn(String phoneNumber) throws SQLException, NotFoundException {
		Customer temp = readCustomerFromDB(phoneNumber);
		if (temp == null)
			throw new NotFoundException(phoneNumber);
		this.theCustomer = temp;
		isLoggedIn = true;
	}

	public void signUp(String firstName, String lastName, String phoneNumber)
			throws AlreadyExistException, SQLException, NotFoundException {
		if (isCustomerExist(phoneNumber))
			throw new AlreadyExistException("Phone number");

		String query = "INSERT into Customer (firstName, lastName, phoneNumber) VALUES (?,?,?)";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, firstName);
		pst.setString(2, lastName);
		pst.setString(3, phoneNumber);
		pst.executeUpdate();
		
		logIn(phoneNumber);
	}

	private boolean isCustomerExist(String phoneNumber) throws SQLException {
		if (readCustomerFromDB(phoneNumber) == null)
			return false;
		return true;
	}

	private Customer readCustomerFromDB(String phoneNumber) throws SQLException {
		String query = "SELECT * FROM customer WHERE phoneNumber = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, phoneNumber);
		ResultSet rs = pst.executeQuery();
		Customer theCustomer = null;

		// ----- Check if any result is not empty -----
		if (!rs.isBeforeFirst())
			return null;

		// ----- Read first (and only) row -----
		rs.next();

		// ----- Read customer data and create customer -----
		int custID = rs.getInt("Id");
		String firstName = rs.getString("firstName");
		String lastName = rs.getString("lastName");
		theCustomer = new Customer(custID, firstName, lastName, phoneNumber);

		// ----- Read appointment data and set to customer -----
		readNextAppointmentFromDB(theCustomer);

		return theCustomer;
	}

	public void readNextAppointmentFromDB(Customer cust) throws SQLException {
		String query = "SELECT * FROM customer_Appointment WHERE customerID = ? AND appointmentTime >= now()";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, cust.getCustomerId());
		ResultSet rs = pst.executeQuery();

		// ----- Check if any result is not empty -----
		if (!rs.isBeforeFirst())
			return;

		// ----- Read first (and only) row -----
		rs.next();

		// ----- Read appointment data, create appointment and set to customer -----
		int appID = rs.getInt("appID");
		LocalDateTime dateAndTime = rs.getTimestamp("appointmentTime").toLocalDateTime();
		String theStyleName = rs.getString("treatment");
		iHaircutStyle theStyle = HairStyleFactory.getDecoratedHairStyle(theStyleName);
		Appointment theAppointment = new Appointment(appID, dateAndTime, theStyle);
		cust.initNextAppointment(theAppointment);

		// ----- Read and set shopping cart date (products) -----
		readAppointmentProductsFromDB(theAppointment);
	}

	public void readAppointmentProductsFromDB(Appointment app) throws SQLException {
		String query = "SELECT * FROM appointmentCart_products WHERE appID = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, app.getId());
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			int prodID = rs.getInt("ID");
			String name = rs.getString("name");
			int price = rs.getInt("price");

			Product p = new Product(prodID, name, price);
			app.initProductInCart(p, rs.getInt("count"));
		}
	}
	
	public void notifyActivityHoursChanged() {
		
	}

	public void logout() {
		_instance = null;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public Customer getTheCustomer() {
		return theCustomer;
	}

}