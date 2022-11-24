package model.msgsObservers;

import java.sql.SQLException;

import model.classes.Customer;
import model.classes.IsraelDayOfWeek;

public interface iManagerObservable {
	void addCustomerObserver(Customer cust);
	void removeCustomerObserverByDay(Customer cust, IsraelDayOfWeek day);
	void notifyCustomerObservers(String msg) throws SQLException; 					
	// notify observers of specific day
	void notifyCustomerByDayObservers(IsraelDayOfWeek dayOfWeek,String msg);
}
