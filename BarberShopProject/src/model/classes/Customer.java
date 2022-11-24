package model.classes;

import java.sql.SQLException;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import model.msgsObservers.*;
import model.singletons.AppointmentScheduleSingleton;

public class Customer extends RecursiveTreeObject<Customer> implements iCustomerObserver {
	private String firstName;
	private String lastName;
	private int customerId;
	private Appointment nextAppointment;
	private String phoneNumber;

	public Customer(int customerId, String firstName, String lastName, String phoneNumber) {
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getCustomerId() {
		return customerId;
	}

	public Appointment getNextAppointment() {
		return nextAppointment;
	}

	// we will call this function when updating customer data from DB
	public void initNextAppointment(Appointment newAppointment) throws SQLException {
		this.nextAppointment = newAppointment;
	}

	// we will call this function if there is no future appointment yet for the
	// customer
	public void setNextAppointment(Appointment newAppointment) throws SQLException {
		Appointment currAppointment = this.getNextAppointment();
		this.nextAppointment = newAppointment;
		if (currAppointment != null) {
			if(isNextAppointment()) {
				this.nextAppointment.setId(currAppointment.getId());
				this.nextAppointment.setProductsCart(currAppointment.getProductsCart());
			}
			AppointmentScheduleSingleton.getInstance().updateAppointmentForCustomer(this, currAppointment);
		}
		else
			AppointmentScheduleSingleton.getInstance().addAppointmentForCustomer(this);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isNextAppointment() {
		return (getNextAppointment() != null);
	}

	public boolean isNextAppointmentInDay(IsraelDayOfWeek day) {
		if(!isNextAppointment())
			return false;
		return (IsraelDayOfWeek.fromJavaDayOfWeek(getNextAppointment().getTimeOfAppointment().getDayOfWeek())).equals(day);
	}
	
	@Override
	public int hashCode() {
		return customerId;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Customer))
			return false;

		Customer temp = (Customer) obj;
		return temp.customerId == this.customerId;
	}

	@Override
	public void updateActivityHoursChanged(String msg) {
		System.out.println("Sending SMS message to " + phoneNumber + ":");
		System.out.println("Hi " + firstName + " " + lastName + ",");
		System.out.println(msg);
		System.out.println("Hope to see you soon, your barber :)");
		System.out.println("------------------------------------------------------------");	
	}

	@Override
	public void updateAppointmentCanceled(IsraelDayOfWeek dayOfWeek, String msg) {
		System.out.println("Sending SMS message to " + phoneNumber + ":");
		System.out.println("'Hi " + firstName + " " + lastName + ",");
		System.out.println(msg);
		System.out.println("Hope to see you soon, your barber :)'");
		System.out.println("------------------------------------------------------------");	
	}

	public String stringName() {
		return firstName +  " "+ lastName;
	}
}
