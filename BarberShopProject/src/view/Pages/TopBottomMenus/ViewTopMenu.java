package view.Pages.TopBottomMenus;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXRippler.RipplerMask;
import com.jfoenix.controls.JFXRippler.RipplerPos;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import view.Pages.iCustomerViewChild;
import view.Pages.RootController.ViewRoot;

public class ViewTopMenu implements iCustomerViewChild {

	private ViewRoot parentController;

	@FXML
	private AnchorPane apTopMenu;
	@FXML
	private JFXHamburger btnHmbMenu;

	private JFXPopup popup;
	private VBox menu;
	private JFXListView<JFXButton> list;
	private Label lblUserName;
	private JFXButton btnHome;
	private JFXButton btnCatalog;
	private JFXButton btnActivityHours;
	private JFXButton btnLogOut;
	
	
	@Override
	public void initializePage() {
		lblUserName = new Label();
		menu = new VBox(lblUserName);
		initMenu();
	}

	@Override
	public void setParentController(ViewRoot parent) {
		parentController = parent;
	}

	private void initMenu() {
		btnHmbMenu.setPadding(new Insets(10, 5, 10, 5));
		JFXRippler rippler = new JFXRippler(btnHmbMenu, RipplerMask.CIRCLE, RipplerPos.BACK);

		apTopMenu.getChildren().add(rippler);
		AnchorPane.setLeftAnchor(rippler, 10.0);
		AnchorPane.setTopAnchor(rippler, 10.0);

		menu = new VBox(lblUserName);
		initMenuButtons();
		popup = new JFXPopup();

		rippler.setOnMouseClicked((e) -> {
			if (!popup.isShowing()) {
				menu.getChildren().clear();
				menu.getChildren().add(getMenuButtonsList());
				popup.setPopupContent(menu);
				popup.show(rippler, PopupVPosition.TOP, PopupHPosition.LEFT, 0, 35);
			} else
				popup.hide();

		});
	}

	private void initMenuButtons() {
		list = new JFXListView<>();

		btnHome = new JFXButton("Home Page");
		btnHome.getStyleClass().clear();
		btnHome.getStyleClass().add("top-bar");
		btnHome.setOnMouseClicked((e) -> {
			popup.hide();
			if (parentController.isLoggedIn())
				parentController.moveToUserHomePage();
			else
				parentController.moveToStartPage();
		});

		btnCatalog = new JFXButton("Catalog");
		btnCatalog.getStyleClass().clear();
		btnCatalog.getStyleClass().add("top-bar");
		btnCatalog.setOnMouseClicked((e) -> {
			popup.hide();
			parentController.moveToCatalogPage();
		});

		btnActivityHours = new JFXButton("Activity Hours");
		btnActivityHours.getStyleClass().clear();
		btnActivityHours.getStyleClass().add("top-bar");
		btnActivityHours.setOnMouseClicked((e) -> {
			popup.hide();
			parentController.moveToActivityHoursPage();
		});

		btnLogOut = new JFXButton("Log Out");
		btnLogOut.getStyleClass().clear();
		btnLogOut.getStyleClass().add("top-bar");
		btnLogOut.setOnMouseClicked((e) -> {
			popup.hide();
			if(parentController.getUser() != null)
				parentController.getUser().logout();

			parentController.moveToStartPage();
		});
	}
	
	private JFXListView<JFXButton> getMenuButtonsList() {
		list.getItems().clear();
		
		list.getItems().add(btnHome);
		list.getItems().add(btnCatalog);
		list.getItems().add(btnActivityHours);

		if (parentController.isLoggedIn()) {
			lblUserName.setText("Hi " + parentController.getUserCustomer().getFirstName());
			lblUserName.getStyleClass().clear();
			lblUserName.getStyleClass().add("label-Top");
			list.getItems().add(btnLogOut);
		}
		else
			lblUserName.setText("Hi Guest");
		
		return list;
	}
	
}
