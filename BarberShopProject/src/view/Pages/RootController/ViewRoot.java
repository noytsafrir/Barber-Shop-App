package view.Pages.RootController;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.classes.Customer;
import model.singletons.UserSingleton;
import mvc_controller.iViewCustomerListenable;
import view.Pages.iCustomerViewChild;
import view.utils.ToastFX;

public class ViewRoot {

	public enum eCustPages{START, SIGN_IN, SIGN_UP, HOME, APPOINTMENT, APPOINTMENT_PRODUCTS, ACTIVITY_HOURS, CATALOG};
	
	@FXML
	private BorderPane bpRoot;

	private Stage toastMsgStage;
	private iCustomerViewChild currChildController;
	private eCustPages currPage;
	private ArrayList<iViewCustomerListenable> allListeners;

	public void initializePage() {
		setTopChildPane("/view/Pages/TopBottomMenus/ViewTopMenu.fxml");
		setBottomChildPane("/view/Pages/TopBottomMenus/ViewBottom.fxml");
		moveToStartPage();
		
		allListeners.get(0).setCustomerViewActiveStatus(true);
		getStage().setOnCloseRequest(e -> allListeners.get(0).setCustomerViewActiveStatus(false));
	}

	public void registerListener(iViewCustomerListenable l) {
		if (allListeners == null) {
			allListeners = new ArrayList<iViewCustomerListenable>();
		}
		allListeners.add(l);
	}
	
	public void removeListener(iViewCustomerListenable l) {
		if (allListeners == null) {
			return;
		}
		allListeners.remove(l);
	}
	
	public void errorMsg(String msg) {
		if(toastMsgStage == null || !toastMsgStage.isShowing())
			toastMsgStage = ToastFX.makeText(getStage(), "\u2022 " + msg, 5000, 500, 500);
		else {
			Label lblToastMsg = (Label)((StackPane)toastMsgStage.getScene().getRoot()).getChildren().get(0);
			lblToastMsg.setText(lblToastMsg.getText() + "\n\u2022 " + msg);
		}
	}

	public Stage getStage() {
		return (Stage) bpRoot.getScene().getWindow();
	}
	
	public iViewCustomerListenable getMvcController() {
		return allListeners.get(0);
	}
	
	public UserSingleton getUser() {
		return getMvcController().getUser();
	}
	
	public boolean isLoggedIn () {
		if(getUser() == null)
			return false;
		
		return getUser().isLoggedIn();
	}

	public Customer getUserCustomer()
	{
		if(getUser() == null)
			return null;
		
		return getUser().getTheCustomer();
	}

	private void setCenterChildPane(String fxmlFileName) {
		FXMLLoader fxml = new FXMLLoader(getClass().getResource(fxmlFileName));
		try {
			AnchorPane childPane = (AnchorPane) fxml.load();
			currChildController = (iCustomerViewChild) fxml.getController();
			currChildController.setParentController(this);
			currChildController.initializePage();
			bpRoot.setCenter(childPane);
		} catch (IOException e) {
			e.printStackTrace();
			errorMsg(e.getMessage());
		}
	}

	private void setTopChildPane(String fxmlFileName) {
		FXMLLoader fxml = new FXMLLoader(getClass().getResource(fxmlFileName));
		try {
			AnchorPane childPane = (AnchorPane) fxml.load();
			currChildController = (iCustomerViewChild) fxml.getController();
			currChildController.setParentController(this);
			currChildController.initializePage();
			bpRoot.setTop(childPane);
		} catch (IOException e) {
			errorMsg(e.getMessage());
		}
	}
	
	private void setBottomChildPane(String fxmlFileName) {
		FXMLLoader fxml = new FXMLLoader(getClass().getResource(fxmlFileName));
		try {
			AnchorPane childPane = (AnchorPane) fxml.load();
			currChildController = (iCustomerViewChild) fxml.getController();
			currChildController.setParentController(this);
			currChildController.initializePage();
			bpRoot.setBottom(childPane);
		} catch (IOException e) {
			errorMsg(e.getMessage());
		}
	}

	public void moveToStartPage() {
		setCenterChildPane("/view/Pages/StartPage/ViewStartPage.fxml");
		currPage = eCustPages.START;
	}

	public void moveToSignUpPage() {
		setCenterChildPane("/view/Pages/StartPage/ViewSignUp.fxml");
		currPage = eCustPages.SIGN_UP;
	}

	public void moveToSignInPage() {
		setCenterChildPane("/view/Pages/StartPage/ViewSignIn.fxml");
		currPage = eCustPages.SIGN_IN;

	}

	public void moveToUserHomePage() {
		setCenterChildPane("/view/Pages/HomePage/ViewUserHomePage.fxml");
		currPage = eCustPages.HOME;
	}

	public void moveToCatalogPage() {
		setCenterChildPane("/view/Pages/CatalogStyles/ViewCatalogStyles.fxml");
		currPage = eCustPages.CATALOG;
	}

	public void moveToActivityHoursPage() {
		setCenterChildPane("/view/Pages/ActivityHours/ViewActivityHours.fxml");
		currPage = eCustPages.ACTIVITY_HOURS;
	}

	public void moveToAddNewAppointment() {
		setCenterChildPane("/view/Pages/Appointment/ViewAppointment.fxml");
		currPage = eCustPages.APPOINTMENT;
	}

	public void moveToMngProducts() {
		setCenterChildPane("/view/Pages/Appointment/ViewAppProducts.fxml");
		currPage = eCustPages.APPOINTMENT_PRODUCTS;
	}

	public void appointmentCanceled(String msg) {
		errorMsg(msg);
		switch(currPage) {
		case APPOINTMENT:
		case APPOINTMENT_PRODUCTS:
		case HOME:
			moveToUserHomePage();
			break;
		default:
			break;
		}
	}
	
	public void activityHoursChanged(String msg) {
		errorMsg(msg);
		switch(currPage) {
		case APPOINTMENT:
			moveToAddNewAppointment();
			break;
		case ACTIVITY_HOURS:
			moveToActivityHoursPage();
			break;
		default:
			break;
		}
	}
}