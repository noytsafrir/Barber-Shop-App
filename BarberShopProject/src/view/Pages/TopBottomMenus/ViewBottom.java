package view.Pages.TopBottomMenus;



import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import view.Pages.iCustomerViewChild;
import view.Pages.RootController.ViewRoot;

public class ViewBottom implements iCustomerViewChild {

	private ViewRoot parentController;

	@FXML
	private AnchorPane apBottom;
	@FXML
	private AnchorPane apNotification;
	@FXML
	private ImageView facebookIcon;

	@FXML
	private ImageView instagramIcon;

	@FXML
	private ImageView phoneIcon;

	@FXML
	private ImageView whatsappIcon;


	@Override
	public void initializePage() {
		apBottom.setVisible(true);
	}

	@Override
	public void setParentController(ViewRoot parent) {
		parentController = parent;
	}
	
	@FXML
	void callByIcon() {
		Stage stage = new Stage();
		WebView web = new WebView();
	   	web.getEngine().load("https://i.stack.imgur.com/4lHXA.jpg");
	   	Scene scene = new Scene(web);
	   	stage.setScene(scene);
	   	stage.show();

	}
	
	@FXML
	void openFacebookByIcon() {
		Stage stage = new Stage();
		WebView web = new WebView();
	   	web.getEngine().load("https://ishadeed.com/assets/fb-login/fb-new-login.png");
	   	Scene scene = new Scene(web);
	   	stage.setScene(scene);
	   	stage.show();

	}
	
	
	@FXML
	void openWhatsappByIcon() {
		Stage stage = new Stage();
		WebView web = new WebView();
	   	web.getEngine().load("https://www.techadvisor.com/cmsdata/features/3595015/whatsapp_computer_thumb800.png");
	   	Scene scene = new Scene(web);
	   	stage.setScene(scene);
	   	stage.show();

	}
	
	
	@FXML
	void openInstagramByIcon() {
		Stage stage = new Stage();
		WebView web = new WebView();
	   	web.getEngine().load("https://www.instagram.com/");
	   	Scene scene = new Scene(web);
	   	stage.setScene(scene);
	   	stage.show();

	}
	
	
	
}
