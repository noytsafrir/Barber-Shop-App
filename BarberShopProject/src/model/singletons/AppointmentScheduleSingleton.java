package model.singletons;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;

import model.classes.Appointment;
import model.classes.Customer;
import model.classes.IsraelDayOfWeek;

public class AppointmentScheduleSingleton {

	private static AppointmentScheduleSingleton _instance = null;
	private static Connection conn;

	private AppointmentScheduleSingleton() throws SQLException {
		conn = DBConnectionSingleton.getInstance().getConnection();
	}

	public static AppointmentScheduleSingleton getInstance() throws SQLException {
		if (_instance == null)
			_instance = new AppointmentScheduleSingleton();
		return _instance;
	}
	
	public ArrayList<LocalTime> getAvailableOptionsTime(IsraelDayOfWeek day, int lenghtOfAppointment, Customer cust)
			throws SQLException {
		LocalTime now 	= LocalTime.now();
		LocalDate today = LocalDate.now();
		LocalTime openHourOfBarberShop 	= ActivityHoursSingleton.getInstance().getBussinessDay(day).getOpenHour();
		LocalTime closeHourOfBarberShop = ActivityHoursSingleton.getInstance().getBussinessDay(day).getCloseHour();
		LocalTime firstOptionForDay 	= openHourOfBarberShop;
		
		// if day is today start from now instead of opening hour
		if (day.getDayOfWeek().equals(today.getDayOfWeek()) && now.isAfter(openHourOfBarberShop)) {
			int timeToNextOption = 20 - (now.getMinute() % 20);
			firstOptionForDay = now.withSecond(0).withNano(0).plusMinutes(timeToNextOption);
		}

		ArrayList<LocalTime> optionsForStartHoursList = new ArrayList<LocalTime>();

		LocalTime startNextAppointment = null;
		LocalTime endPrevAppointment = null;
		LocalTime endRequstedAppointment = null;
		// select from a view that contain only days , start and end hours of existing
		// appointments
		String query = "SELECT * FROM appointment_start_end_hour WHERE dayOfWeek = ? AND customer != ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, day.getDayNum());
		pst.setInt(2, cust.getCustomerId());
		ResultSet rs = pst.executeQuery();
		// read the first row (appointment data)
		if (!rs.next()) {
			// if there are no appointments -> return all day options
			return getOptionsOfStartHourForTheList(firstOptionForDay,
					closeHourOfBarberShop.minusMinutes(lenghtOfAppointment));
		}
		endPrevAppointment = firstOptionForDay;
		do {
			// calculate the end time of requested appointment
			endRequstedAppointment = endPrevAppointment.plusMinutes(lenghtOfAppointment);
			// read from DB the start time of next appointment
			startNextAppointment = rs.getTime("startHour").toLocalTime();

			if (!endRequstedAppointment.isAfter(startNextAppointment))
				// if there is enough time for requested appointment -> get all options
				optionsForStartHoursList.addAll(getOptionsOfStartHourForTheList(endPrevAppointment,
						startNextAppointment.minusMinutes(lenghtOfAppointment)));

			// read from DB the end of current checked appointment
			endPrevAppointment = rs.getTime("endHour").toLocalTime();
		} while (rs.next());

		startNextAppointment = closeHourOfBarberShop;
		if (!endRequstedAppointment.isAfter(startNextAppointment))
			optionsForStartHoursList.addAll(getOptionsOfStartHourForTheList(endPrevAppointment,
					startNextAppointment.minusMinutes(lenghtOfAppointment)));

		return optionsForStartHoursList;
	}

	private ArrayList<LocalTime> getOptionsOfStartHourForTheList(LocalTime start, LocalTime end) {
		ArrayList<LocalTime> allOptions = new ArrayList<LocalTime>();
		while (!start.isAfter(end)) {
			allOptions.add(start);
			start = start.plusMinutes(20);
		}
		return allOptions;
	}

	public synchronized void addAppointmentForCustomer(Customer theCustomer) throws SQLException {
		String query = "INSERT into appointment (customerID, appointmentTime, appointmentLenInMin, price, treatment) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, theCustomer.getCustomerId());
		pst.setTimestamp(2, Timestamp.valueOf(theCustomer.getNextAppointment().getTimeOfAppointment()));
		pst.setInt(3, theCustomer.getNextAppointment().getChoosenStyle().getTotalLenght());
		pst.setInt(4, theCustomer.getNextAppointment().getChoosenStyle().getTotalPrice());
		pst.setString(5, theCustomer.getNextAppointment().getChoosenStyle().getAllStylesStrings());
		if (pst.executeUpdate() != 1)
			throw new SQLException();
		
		readAppointmentIDFromDB(theCustomer, theCustomer.getNextAppointment());
		ActivityHoursSingleton.getInstance().addCustomerObserver(theCustomer);
	}
	
	public void readAppointmentIDFromDB(Customer cust, Appointment app) throws SQLException {
		String query = "SELECT * FROM customer_Appointment WHERE customerID = ? AND appointmentTime = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, cust.getCustomerId());
		pst.setTimestamp(2, Timestamp.valueOf(app.getTimeOfAppointment()));
		ResultSet rs = pst.executeQuery();
		while (rs.next())
			app.setId(rs.getInt("appID"));
	}
	
	public synchronized void updateAppointmentForCustomer(Customer theCustomer, Appointment prevAppointment) throws SQLException {
		if (theCustomer.getNextAppointment() != null) {
			String query = "UPDATE appointment SET appointmentTime = ?, appointmentLenInMin = ?, price = ?, treatment = ? WHERE appID = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setTimestamp(1, Timestamp.valueOf(theCustomer.getNextAppointment().getTimeOfAppointment()));
			pst.setInt(2, theCustomer.getNextAppointment().getChoosenStyle().getTotalLenght());
			pst.setInt(3, theCustomer.getNextAppointment().getChoosenStyle().getTotalPrice());
			pst.setString(4, theCustomer.getNextAppointment().getChoosenStyle().getAllStylesStrings());
			pst.setInt(5, theCustomer.getNextAppointment().getId());
			if (pst.executeUpdate() != 1)
				throw new SQLException();

			ActivityHoursSingleton.getInstance().addCustomerObserver(theCustomer);
		} else {
			String query = "DELETE FROM appointment WHERE appId = ?;";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, prevAppointment.getId());

			if (pst.executeUpdate() != 1)
				throw new SQLException();
		}
		ActivityHoursSingleton.getInstance().removeCustomerObserverByDay(theCustomer, prevAppointment.getDayOfWeek());
	}
	
}
