package view.Pages.HomePage;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.classes.Customer;
import view.Pages.iCustomerViewChild;
import view.Pages.RootController.ViewRoot;

public class ViewUserHomePage implements iCustomerViewChild {

	private ViewRoot parentController;
	private Customer cust;

    @FXML
    private AnchorPane apNextAppointment;
    @FXML
    private AnchorPane apNoAppointment;
    @FXML
    private JFXButton btnCancelAppointment;
    @FXML
    private JFXButton btnEditAppointment;
    @FXML
    private JFXButton btnProducts;
    @FXML
    private JFXButton btnNewAppointment;
    @FXML
    private Label lblHiMsgUser;
    @FXML
    private Label lblNextAppDate;
    @FXML
    private Label lblNextAppPrice;
    @FXML
    private Label lblNextAppStyle;
    @FXML
    private Label lblNextAppTime;
    @FXML
    private Label lblTotalPrice;

    @Override
    public void initializePage() {
    	cust = parentController.getUserCustomer();
		if(cust == null) {
			parentController.moveToStartPage();
			return;
		}
		
    	initializeHomePage();
    	btnEditAppointment.setFocusTraversable(false);
    	btnCancelAppointment.setFocusTraversable(false);
    	btnProducts.setFocusTraversable(false);
    	btnNewAppointment.setFocusTraversable(false);
    }

    @Override
    public void setParentController(ViewRoot parent) {
    	parentController = parent;		
    }
    
    private void initializeHomePage() {
		apNoAppointment.setVisible(false);
		apNextAppointment.setVisible(false);

		lblHiMsgUser.setText("Hi " + cust.getFirstName() + " " + cust.getLastName());
		lblHiMsgUser.getStyleClass().clear();
		lblHiMsgUser.getStyleClass().add("label-Top");
		if(!cust.isNextAppointment())
			apNoAppointment.setVisible(true);
		else {
			String time = cust.getNextAppointment().getTimeOfAppointment().format(DateTimeFormatter.ofPattern("HH:mm"))
							+ " - " + 
						  cust.getNextAppointment().getTimeOfEndAppointment().format(DateTimeFormatter.ofPattern("HH:mm"));
			String date = cust.getNextAppointment().getTimeOfAppointment().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String hairStyle = cust.getNextAppointment().getChoosenStyle().getAllStylesStrings();
			int price = cust.getNextAppointment().getChoosenStyle().getTotalPrice();
			int totalPrice = cust.getNextAppointment().sumProductsCost();
			apNextAppointment.setVisible(true);
			lblNextAppDate.setText(date);
			lblNextAppTime.setText(time);
			lblNextAppStyle.setText(hairStyle);
			lblNextAppPrice.setText(price + " ¤");
			lblTotalPrice.setText(totalPrice + " ¤");
		}
	}
    
    @FXML
    void btnActionNewAppointment() {
    	parentController.moveToAddNewAppointment();
    }

    @FXML
    void btnActionEditAppointment() {
    	parentController.moveToAddNewAppointment();
    }
    
    @FXML
    void btnActionCancelAppointment() throws SQLException {
    	cust.setNextAppointment(null);
    	parentController.moveToUserHomePage();
    }
    
    @FXML
    void btnActionMngProducts() {
    	parentController.moveToMngProducts();;
    }

}
