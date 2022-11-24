package mvc_controller;

import java.util.HashSet;

import model.classes.Product;
import model.singletons.*;

public interface iViewCustomerListenable {
	//listener to the customer view
	//if the view of the customer will notify a change  ---> get a message 

	public void setCustomerViewActiveStatus(Boolean status);
	public UserSingleton getUser();
	public HashSet<Product> getAllProducts();
	public ActivityHoursSingleton getActivityHoursMng();
	public CatalogStylesSingleton getCatalogMng();
	public AppointmentScheduleSingleton getScheduleMng();


}
