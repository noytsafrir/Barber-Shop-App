package model.singletons;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.classes.Appointment;
import model.classes.BusinessDay;
import model.classes.Customer;
import model.classes.IsraelDayOfWeek;
import model.exceptions.NotFoundException;
import model.haircutDecorator.HairStyleFactory;
import model.haircutDecorator.iHaircutStyle;
import model.msgsObservers.*;
import mvc_controller.iModelListenable;

public class ActivityHoursSingleton implements iManagerObservable {

	private static ActivityHoursSingleton _instance = null;
	private static Connection conn;
	private static HashMap<IsraelDayOfWeek, BusinessDay> activitiesDaysList;
	private HashSet<iCustomerObserver> allCustomersObservers;

	private static HashMap<IsraelDayOfWeek, Set<Customer>> customersObserversByDays;
	private static ArrayList<Message> msgs;
	private ArrayList<iModelListenable> allListeners;

	private ActivityHoursSingleton() throws SQLException {
		conn = DBConnectionSingleton.getInstance().getConnection();
		activitiesDaysList = new HashMap<IsraelDayOfWeek, BusinessDay>();
		allCustomersObservers = new HashSet<iCustomerObserver>();
		msgs = new ArrayList<>();
		activitiesDaysList.put(IsraelDayOfWeek.SUNDAY, new BusinessDay(IsraelDayOfWeek.SUNDAY));
		activitiesDaysList.put(IsraelDayOfWeek.MONDAY, new BusinessDay(IsraelDayOfWeek.MONDAY));
		activitiesDaysList.put(IsraelDayOfWeek.TUESDAY, new BusinessDay(IsraelDayOfWeek.TUESDAY));
		activitiesDaysList.put(IsraelDayOfWeek.WEDNESDAY, new BusinessDay(IsraelDayOfWeek.WEDNESDAY));
		activitiesDaysList.put(IsraelDayOfWeek.THURSDAY, new BusinessDay(IsraelDayOfWeek.THURSDAY));
		activitiesDaysList.put(IsraelDayOfWeek.FRIDAY, new BusinessDay(IsraelDayOfWeek.FRIDAY));
		activitiesDaysList.put(IsraelDayOfWeek.SATURDAY, new BusinessDay(IsraelDayOfWeek.SATURDAY));

		customersObserversByDays = new HashMap<IsraelDayOfWeek, Set<Customer>>();
		customersObserversByDays.put(IsraelDayOfWeek.SUNDAY, new HashSet<Customer>());
		customersObserversByDays.put(IsraelDayOfWeek.MONDAY, new HashSet<Customer>());
		customersObserversByDays.put(IsraelDayOfWeek.TUESDAY, new HashSet<Customer>());
		customersObserversByDays.put(IsraelDayOfWeek.WEDNESDAY, new HashSet<Customer>());
		customersObserversByDays.put(IsraelDayOfWeek.THURSDAY, new HashSet<Customer>());
		customersObserversByDays.put(IsraelDayOfWeek.FRIDAY, new HashSet<Customer>());
		customersObserversByDays.put(IsraelDayOfWeek.SATURDAY, new HashSet<Customer>());
		readAllDaysDataFromDB();
		readAllCustObserversFromDB();
	}

	public static ActivityHoursSingleton getInstance() throws SQLException {
		if (_instance == null)
			_instance = new ActivityHoursSingleton();
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

	public BusinessDay getBussinessDay(IsraelDayOfWeek day) {
		return activitiesDaysList.get(day);
	}

	private void readAllDaysDataFromDB() throws SQLException {
		for (HashMap.Entry<IsraelDayOfWeek, BusinessDay> entry : activitiesDaysList.entrySet()) {
			readDayDataFromDB(entry.getValue());
		}
	}

	private void readDayDataFromDB(BusinessDay day) throws SQLException {
		String query = "SELECT * FROM ActivityTime WHERE DayOfWeek = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, day.getTheDay().getDayNum());
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			day.setOpenHour(rs.getTime("openHour").toLocalTime());
			day.setCloseHour(rs.getTime("closeHour").toLocalTime());

		}
	}

	public void updateActivityHoursInDB(IsraelDayOfWeek day, LocalTime openTime, LocalTime closeTime)
			throws SQLException {
		String query = "UPDATE ActivityTime SET openHour = ? , closeHour = ? WHERE dayOfWeek = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setTime(1, Time.valueOf(openTime));
		pst.setTime(2, Time.valueOf(closeTime));
		pst.setInt(3, day.getDayNum());

		int numOfAffectedRows = pst.executeUpdate();
		if (numOfAffectedRows != 1)
			throw new SQLException();
		readAllDaysDataFromDB();

		if (openTime.equals(LocalTime.of(0, 0)) && closeTime.equals(LocalTime.of(0, 0)))
			setDayOff(day);
	}

	private void setDayOff(IsraelDayOfWeek day) throws SQLException {
		String query = "DELETE FROM appointment WHERE dayofweek(appointmentTime) = ? AND appointmentTime >= now()";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, day.getDayNum());
		pst.executeUpdate();

		
		String msgText = "Your appointment at " + day.getDisplayName()
		+ " has been canceled due to a barber day off";
		
		notifyCustomerByDayObservers(day, msgText);
		getAllCustomersInAppInDay(day).clear();
		readAllDaysDataFromDB();
		readAllCustObserversFromDB();
		
		UserSingleton user = UserSingleton.getInstance();
		boolean isAppointmentCanceled = false; 
		if (user.isLoggedIn()) {
			try {
				if(user.getTheCustomer().isNextAppointmentInDay(day))
					isAppointmentCanceled = true;
				UserSingleton.getInstance().logIn(UserSingleton.getInstance().getTheCustomer().getPhoneNumber());
				if(isAppointmentCanceled)
					for (iModelListenable l : allListeners)
						l.appointmentCanceled(msgText);
			} catch (NotFoundException e) {
				UserSingleton.getInstance().logout();
				for (iModelListenable l : allListeners)
					l.forceLogout("Error processing user data, logging out");
			}
		}
		
	}

	public ArrayList<IsraelDayOfWeek> getAvailableOptionsDay() {
		ArrayList<IsraelDayOfWeek> optionsForDays = new ArrayList<IsraelDayOfWeek>();
		ArrayList<IsraelDayOfWeek> thisWeek = new ArrayList<IsraelDayOfWeek>();
		ArrayList<IsraelDayOfWeek> nextWeek = new ArrayList<IsraelDayOfWeek>();

		for (HashMap.Entry<IsraelDayOfWeek, BusinessDay> entry : activitiesDaysList.entrySet()) {
			if (entry.getValue().isAvailable())
				optionsForDays.add(entry.getKey());
		}

		Collections.sort(optionsForDays);

		for (IsraelDayOfWeek day : optionsForDays) {
			if (day.compareTo(IsraelDayOfWeek.fromJavaDayOfWeek(LocalDate.now().getDayOfWeek())) >= 0)
				thisWeek.add(day);
			else
				nextWeek.add(day);
		}

		optionsForDays.clear();
		optionsForDays.addAll(thisWeek);
		optionsForDays.addAll(nextWeek);

		return optionsForDays;
	}

	public ArrayList<BusinessDay> getAllBusinessDays() {
		ArrayList<BusinessDay> daysList = new ArrayList<BusinessDay>();
		daysList.addAll(activitiesDaysList.values());
		Collections.sort(daysList);
		return daysList;
	}

	public void readAllCustObserversFromDB() throws SQLException {
		clearObservers();
		String query = "SELECT * FROM customer";
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		Customer theCustomer = null;
		while (rs.next()) {
			// ----- Read & create customer -----
			int custID = rs.getInt("ID");
			String firstName = rs.getString("firstName");
			String lastName = rs.getString("lastName");
			String phoneNumber = rs.getString("phoneNumber");
			theCustomer = new Customer(custID, firstName, lastName, phoneNumber);

			readNextAppointmentFromDB(theCustomer);

			// ----- Add customer as observer -----
			addCustomerObserver(theCustomer);
		}
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
	}

	public void addCustomerObserver(Customer cust) {
		IsraelDayOfWeek day = null;
		if (cust.isNextAppointment()) {
			day = cust.getNextAppointment().getDayOfWeek();
			Set<Customer> custListDay = customersObserversByDays.get(day);
			custListDay.add(cust);
		}

		allCustomersObservers.add(cust);
		if(allListeners != null)
			for (iModelListenable l : allListeners)
				l.customerAppointmentChanged();
	}

	public void clearObservers() {
		allCustomersObservers.clear();
		for (Map.Entry<IsraelDayOfWeek, Set<Customer>> entry : customersObserversByDays.entrySet())
			entry.getValue().clear();
	}

	@Override
	public void removeCustomerObserverByDay(Customer cust, IsraelDayOfWeek day) {
		customersObserversByDays.get(day).remove(cust);
		if(allListeners != null)
			for (iModelListenable l : allListeners)
				l.customerAppointmentChanged();
	}

	@Override
	public void notifyCustomerObservers(String msg) throws SQLException {
		String msgText = "The activity hours has changed to:\n" + activityHourstoString();
		
		for (iCustomerObserver cust : allCustomersObservers) {
			cust.updateActivityHoursChanged(msgText);
			msgs.add(new Message(cust, msgText, "Activity hours has changed", LocalDateTime.now()));
		}
	}

	@Override
	public void notifyCustomerByDayObservers(IsraelDayOfWeek day, String msg) {
		for (Customer cust : customersObserversByDays.get(day)) {
			Message custMsg = new Message(cust, msg, "Appoinment on " + day.getDisplayName() + " canceled",
					LocalDateTime.now());
			cust.updateAppointmentCanceled(day, msg);
			msgs.add(custMsg);
		}
	}

	public String activityHourstoString() {
		StringBuffer str = new StringBuffer();
		for (HashMap.Entry<IsraelDayOfWeek, BusinessDay> entry : activitiesDaysList.entrySet()) {
			str.append(entry.getValue().toStringDetails());
			str.append("\n");
		}
		return str.toString();
	}

	public Set<Customer> getAllCustomersInAppInDay(IsraelDayOfWeek theDay) {
		return customersObserversByDays.get(theDay);
	}

	public ArrayList<Message> getMsgs() {
		return msgs;
	}
}
