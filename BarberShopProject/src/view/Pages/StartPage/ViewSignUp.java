package view.Pages.StartPage;

import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.exceptions.AlreadyExistException;
import model.exceptions.InvalidInputException;
import model.exceptions.NotFoundException;
import model.singletons.UserSingleton;
import view.Pages.iCustomerViewChild;
import view.Pages.RootController.ViewRoot;
import view.utils.Validators;

public class ViewSignUp implements iCustomerViewChild {

	private ViewRoot parentController;
	
    @FXML
    private AnchorPane apSignUp;
    @FXML
    private JFXButton btnSignUp;
    @FXML
    private JFXButton btnBack;
    @FXML
    private Label lblMsgError;
    @FXML
    private TextField tfFName;
    @FXML
    private TextField tfLName;
    @FXML
    private TextField tfPostPhone;
    @FXML
    private TextField tfPrePhone;

    @Override
    public void initializePage() {
    	btnSignUp.setFocusTraversable(false);
    	btnBack.setFocusTraversable(false);
    	resetTextFields();
    	resetSignInErrorMsg();
    }

    @Override
    public void setParentController(ViewRoot parent) {
    	parentController = parent;		
    }

    @FXML
    void btnActionSignUp() {
    	signUpFunction();
    }
    
    @FXML
	void clearTextField(MouseEvent event) {
		((TextField) event.getSource()).clear();
	}
    
    @FXML
    void btnActionBack() {
		parentController.moveToStartPage();			
    }
    
    private void signUpFunction() {
		String firstName = tfFName.getText();
		String lastName = tfLName.getText();
		String prePhone = tfPrePhone.getText();
		String postPhone = tfPostPhone.getText();
		try {
			Validators.isPhoneNumberSignInValid(prePhone, postPhone);
			Validators.isFirstNameValid(firstName);
			Validators.isLastNameValid(lastName);
			String phoneNumber= combinePhoneNumber(prePhone, postPhone);
			UserSingleton.getInstance().signUp(firstName,lastName, phoneNumber);
			resetSignInErrorMsg();
			parentController.moveToUserHomePage();			
		} catch (InvalidInputException | AlreadyExistException | SQLException | NotFoundException e) {
			setSignUpErrorMsg(e.getMessage());
		}
	}
    
	private String combinePhoneNumber(String prePhone, String postPhone) {
		return prePhone+ '-' +postPhone;
	}

	private void setSignUpErrorMsg(String msg) {
		lblMsgError.setText(msg);
		if (!lblMsgError.isVisible())
			lblMsgError.setVisible(true);
	}
	
	private void resetSignInErrorMsg() {
		lblMsgError.setText("");
		if (lblMsgError.isVisible())
			lblMsgError.setVisible(false);
	}
	
	private void resetTextFields() {
		tfFName.clear();
		tfLName.clear();
		tfPrePhone.clear();
		tfPostPhone.clear();
		
		tfPrePhone.textProperty().addListener((observable, oldValue, newValue) -> {
			if (tfPrePhone.getText().length() == 3) {
				if (tfPostPhone.getText().length() < 7)
					tfPostPhone.requestFocus();
				else
					btnSignUp.requestFocus();
			}
		});
		
		tfPostPhone.textProperty().addListener((observable, oldValue, newValue) -> {
			if (tfPostPhone.getText().length() == 7) {
				if (tfPrePhone.getText().length() != 3)
					tfPrePhone.requestFocus();
				else
					btnSignUp.requestFocus();
			}
		});
	}
}
