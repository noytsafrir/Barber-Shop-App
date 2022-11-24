package mvc_controller;

import java.sql.SQLException;
import java.util.HashSet;

import model.classes.*;
import model.singletons.*;
import view.Pages.ManagerSide.ManagerView;
import view.Pages.RootController.ViewRoot;

public class Controller implements iModelListenable, iViewCustomerListenable, iViewManagerListenable {
	private ViewRoot viewCustomer;
	private ManagerView viewManager;
	private boolean managerViewActive;
	private boolean customerViewActive;

	public Controller(ViewRoot viewCustomerView, ManagerView viewManager) {
		this.viewCustomer = viewCustomerView;
		this.viewCustomer.registerListener(this);	// view listener customer
		this.viewCustomer.initializePage();			

		this.viewManager = viewManager;
		this.viewManager.registerListener(this);	// view listener manager
		this.viewManager.initializePage();

	}

	public void setCustomerViewActiveStatus(Boolean status) {
		customerViewActive = status;
	}

	public void setManagerViewActiveStatus(Boolean status) {
		managerViewActive = status;
	}

	@Override
	public UserSingleton getUser() {
		try {
			UserSingleton user = UserSingleton.getInstance();
			user.registerListener(this);
			return user;
		} catch (SQLException e) {
			notifyErrorMsg(e.getMessage());
			return null;
		}
	}

	@Override
	public HashSet<Product> getAllProducts() {
		try {
			return BarberMngSingleton.getInstance().getAllProducts();
		} catch (SQLException e) {
			notifyErrorMsg(e.getMessage());
			return null;
		}
	}

	@Override
	public ActivityHoursSingleton getActivityHoursMng() {
		try {
			ActivityHoursSingleton activityMng = ActivityHoursSingleton.getInstance();
			activityMng.registerListener(this);
			return activityMng;
		} catch (SQLException e) {
			notifyErrorMsg(e.getMessage());
			viewCustomer.moveToUserHomePage();
			return null;
		}
	}

	@Override
	public CatalogStylesSingleton getCatalogMng() {
		try {
			CatalogStylesSingleton catalogMng = CatalogStylesSingleton.getInstance();
			catalogMng.registerListener(this);
			return catalogMng;
		} catch (SQLException e) {
			notifyErrorMsg(e.getMessage());
			if(customerViewActive)
				viewCustomer.moveToUserHomePage();
			return null;
		}
	}

	@Override
	public AppointmentScheduleSingleton getScheduleMng() {
		try {
			return AppointmentScheduleSingleton.getInstance();
		} catch (SQLException e) {
			notifyErrorMsg(e.getMessage());
			if(customerViewActive)
				viewCustomer.moveToUserHomePage();
			return null;
		}
	}

	@Override
	public void notifyErrorMsg(String msg) {
		if(customerViewActive)
			viewCustomer.errorMsg(msg);
	}

	@Override
	public void activityHoursChanged(String msg) {
		if(customerViewActive)
			viewCustomer.activityHoursChanged(msg);
	}

	@Override
	public void appointmentCanceled(String msg) {
		if(customerViewActive) {
			viewCustomer.appointmentCanceled(msg);

		}
	}

	@Override
	public void forceLogout(String msg) {
		if(customerViewActive) {
			notifyErrorMsg(msg);
			viewCustomer.moveToStartPage();
		}
	}

	@Override
	public void customerAppointmentChanged() {
		if(managerViewActive) {
			viewManager.initAppointmentsTableView();
			viewManager.initAppointmentsNextDayTableView();
		}
	}

}
