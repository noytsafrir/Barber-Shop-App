package view.Pages.StartPage;

import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.exceptions.*;
import model.singletons.UserSingleton;
import view.Pages.iCustomerViewChild;
import view.Pages.RootController.ViewRoot;
import view.utils.Validators;

public class ViewSignIn implements iCustomerViewChild {

	private ViewRoot parentController;

	@FXML
	private AnchorPane apSignIn;
	@FXML
	private JFXButton btnSignIn;
	@FXML
	private JFXButton btnBack;
	@FXML
	private Label lblMsgError;
	@FXML
	private TextField tfPostPhone;
	@FXML
	private TextField tfPrePhone;

	@Override
	public void initializePage() {
		btnSignIn.setFocusTraversable(false);
		btnBack.setFocusTraversable(false);
		resetTextFields();
		resetSignInErrorMsg();
	}

	@Override
	public void setParentController(ViewRoot parent) {
		parentController = parent;
	}

	@FXML
	void btnActionSignIn() {
		signInFunction();
	}

	@FXML
	void clearTextField(MouseEvent event) {
		((TextField) event.getSource()).clear();
	}

	@FXML
	void btnActionBack() {
		parentController.moveToStartPage();
	}

	private void signInFunction() {
		String prePhone = tfPrePhone.getText();
		String postPhone = tfPostPhone.getText();

		try {
			Validators.isPhoneNumberSignInValid(prePhone, postPhone);
			String phoneNumber = combinePhoneNumber(prePhone, postPhone);
			UserSingleton.getInstance().logIn(phoneNumber);
			resetSignInErrorMsg();
			parentController.moveToUserHomePage();
		} catch (SQLException | InvalidInputException | NotFoundException e) {
			setSignUpErrorMsg(e.getMessage());
		}
	}

	private String combinePhoneNumber(String prePhone, String postPhone) {
		return prePhone + '-' + postPhone;
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
		tfPrePhone.clear();
		tfPostPhone.clear();

		tfPrePhone.textProperty().addListener((observable, oldValue, newValue) -> {
			if (tfPrePhone.getText().length() == 3) {
				if (tfPostPhone.getText().length() < 7)
					tfPostPhone.requestFocus();
				else
					btnSignIn.requestFocus();
			}
		});
		
		tfPostPhone.textProperty().addListener((observable, oldValue, newValue) -> {
			if (tfPostPhone.getText().length() == 7) {
				if (tfPrePhone.getText().length() != 3)
					tfPrePhone.requestFocus();
				else
					btnSignIn.requestFocus();
			}
		});
	}
}
