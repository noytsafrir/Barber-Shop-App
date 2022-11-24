package view.Pages.ActivityHours;


import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import model.classes.BusinessDay;
import model.singletons.ActivityHoursSingleton;
import view.Pages.iCustomerViewChild;
import view.Pages.RootController.ViewRoot;

public class ViewActivityHours implements iCustomerViewChild {

	private ViewRoot parentController;
	private ActivityHoursSingleton activityMng;

	@FXML
	private JFXTreeTableView<BusinessDay> tvActivityHours;

	@Override
	public void initializePage() {
    	activityMng = parentController.getMvcController().getActivityHoursMng();
    	if(activityMng == null) {
			parentController.moveToUserHomePage();
			return;
    	}
    	
		initAvtivityHoursTableView();
	}

	@Override
	public void setParentController(ViewRoot parent) {
		parentController = parent;
	}

	public void initAvtivityHoursTableView() {
		JFXTreeTableColumn<BusinessDay, String> day = new JFXTreeTableColumn<>("Day");
		day.setMinWidth(80);
		day.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<BusinessDay, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<BusinessDay, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().toString());
					}
				});

		JFXTreeTableColumn<BusinessDay, String> openHour = new JFXTreeTableColumn<>("Opening Hour");
		openHour.setMinWidth(110);
		openHour.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<BusinessDay, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<BusinessDay, String> param) {
						return new SimpleStringProperty((param.getValue().getValue().isAvailable()
								? param.getValue().getValue().toStringOpenHour()
								: "Closed"));
					}
				});

		JFXTreeTableColumn<BusinessDay, String> closeHour = new JFXTreeTableColumn<>("Closing Hour");
		closeHour.setMinWidth(110);
		closeHour.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<BusinessDay, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<BusinessDay, String> param) {
						return new SimpleStringProperty((param.getValue().getValue().isAvailable()
								? param.getValue().getValue().toStringCloseHour()
								: ""));
					}
				});

		day.setStyle("-fx-alignment: CENTER;");
		openHour.setStyle("-fx-alignment: CENTER;");
		closeHour.setStyle("-fx-alignment: CENTER;");

		ObservableList<BusinessDay> days = null;
		days = FXCollections.observableArrayList(activityMng.getAllBusinessDays());

		final TreeItem<BusinessDay> root = new RecursiveTreeItem<BusinessDay>(days, RecursiveTreeObject::getChildren);
		tvActivityHours.getColumns().setAll(day, openHour, closeHour);
		tvActivityHours.setRoot(root);
		tvActivityHours.setShowRoot(false);

	}

}