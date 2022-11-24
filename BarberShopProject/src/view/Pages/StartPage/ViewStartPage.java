package view.Pages.StartPage;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import view.Pages.iCustomerViewChild;
import view.Pages.RootController.ViewRoot;

public class ViewStartPage implements iCustomerViewChild {

	private ViewRoot parentController;
    
	@FXML
    private AnchorPane apStart;
    @FXML
    private JFXButton btnActionSignIn;
    @FXML
    private JFXButton btnActionSignUp;

	@Override
	public void initializePage() {
		btnActionSignIn.setFocusTraversable(false);
		btnActionSignUp.setFocusTraversable(false);
	}
    

    @Override
    public void setParentController(ViewRoot parent) {
    	parentController = parent;
    }
    
    @FXML
    void btnActionSignIn() {
    	parentController.moveToSignInPage();
    }

    @FXML
    void btnActionSignUp() {
    	parentController.moveToSignUpPage();
    }

	
 }
