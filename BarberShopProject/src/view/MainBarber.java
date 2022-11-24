package view;
	

import javafx.application.Application;
import javafx.stage.Stage;
//import jfxtras.styles.jmetro.JMetro;
//import jfxtras.styles.jmetro.Style;
import mvc_controller.Controller;
import view.Pages.ManagerSide.ManagerView;
import view.Pages.RootController.ViewRoot;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class MainBarber extends Application {
	
	Controller mvc;
	
	@Override
	public void start(Stage primaryStage) {
		try {

			FXMLLoader fxmlCustomer = new FXMLLoader(getClass().getResource("/view/Pages/RootController/ViewRoot.fxml"));
			BorderPane bpRootCustomer = (BorderPane) fxmlCustomer.load();
			ViewRoot viewRootCustController = (ViewRoot) fxmlCustomer.getController();
			Scene sceneCustomer = new Scene(bpRootCustomer,355,600);
			sceneCustomer.getStylesheets().add(getClass().getResource("cssStyle/style.css").toExternalForm());
			primaryStage.setScene(sceneCustomer);

			FXMLLoader fxmlManager = new FXMLLoader(getClass().getResource("/view/Pages/ManagerSide/ManagerView.fxml"));
			VBox vbRootManager = (VBox)fxmlManager.load();
			Stage managerStage = new Stage();
			ManagerView viewRootMngController = (ManagerView) fxmlManager.getController();
			Scene sceneManager = new Scene(vbRootManager,1280,800);
			managerStage.setScene(sceneManager);
			
			mvc = new Controller(viewRootCustController, viewRootMngController);
			primaryStage.show();
			managerStage.show();


		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}