package view.Pages.ManagerSide;

import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.classes.*;
import model.classes.StyleType.eStyleTypes;
import model.msgsObservers.Message;
import model.singletons.*;
import mvc_controller.iViewManagerListenable;

public class ManagerView {

    @FXML
    private VBox vbManagerRoot;
	@FXML
	private JFXButton btnActivityManager;
	@FXML
	private JFXButton btnStylesManager;
	@FXML
	private JFXButton btnAddProduct;
	@FXML
	private JFXButton btnSaveProd;
	@FXML
	private JFXButton btnAddProd;
	@FXML
	private JFXComboBox<LocalTime> cmbCloseFriday;
	@FXML
	private JFXComboBox<LocalTime> cmbCloseMonday;
	@FXML
	private JFXComboBox<LocalTime> cmbCloseSaturday;
	@FXML
	private JFXComboBox<LocalTime> cmbCloseSunday;
	@FXML
	private JFXComboBox<LocalTime> cmbCloseThursday;
	@FXML
	private JFXComboBox<LocalTime> cmbCloseTuesday;
	@FXML
	private JFXComboBox<LocalTime> cmbCloseWednesday;
	@FXML
	private JFXComboBox<LocalTime> cmbOpenFriday;
	@FXML
	private JFXComboBox<LocalTime> cmbOpenMonday;
	@FXML
	private JFXComboBox<LocalTime> cmbOpenSaturday;
	@FXML
	private JFXComboBox<LocalTime> cmbOpenSunday;
	@FXML
	private JFXComboBox<LocalTime> cmbOpenThursday;
	@FXML
	private JFXComboBox<LocalTime> cmbOpenTuesday;
	@FXML
	private JFXComboBox<LocalTime> cmbOpenWednesday;
	@FXML
	private JFXComboBox<Integer> cmbLengthBeard;
	@FXML
	private JFXComboBox<Integer> cmbLengthChild;
	@FXML
	private JFXComboBox<Integer> cmbLengthColor;
	@FXML
	private JFXComboBox<Integer> cmbLengthEdges;
	@FXML
	private JFXComboBox<Integer> cmbLengthMan;
	@FXML
	private JFXComboBox<Integer> cmbLengthShades;
	@FXML
	private JFXComboBox<Integer> cmbLengthStraight;
	@FXML
	private JFXComboBox<Integer> cmbPriceBeard;
	@FXML
	private JFXComboBox<Integer> cmbPriceChild;
	@FXML
	private JFXComboBox<Integer> cmbPriceColor;
	@FXML
	private JFXComboBox<Integer> cmbPriceEdges;
	@FXML
	private JFXComboBox<Integer> cmbPriceMan;
	@FXML
	private JFXComboBox<Integer> cmbPriceShades;
	@FXML
	private JFXComboBox<Integer> cmbPriceStraight;
	@FXML
	private JFXCheckBox ckb1;
	@FXML
	private JFXCheckBox ckb2;
	@FXML
	private JFXCheckBox ckb3;
	@FXML
	private JFXCheckBox ckb4;
	@FXML
	private JFXCheckBox ckb5;
	@FXML
	private JFXCheckBox ckb6;
	@FXML
	private JFXCheckBox ckb7;
	@FXML
	private JFXTreeTableView<Customer> tvManagerApp;
	@FXML
	private JFXTreeTableView<Customer> tvManagerAppNextDay;
	@FXML
	private JFXTreeTableView<Message> tvManagerMassages;
	@FXML
	private JFXTreeTableView<Product> tvProducts;
	@FXML
	private JFXComboBox<Integer> cmbProdPrice;
	@FXML
	private Label lbProdId;
	@FXML
	private TextField tfProdName;

	private ArrayList<iViewManagerListenable> allListeners;

	public void initializePage() {
		try {
			initCmbHours();
			initStyle();
			initAppointmentsTableView();
			initAppointmentsNextDayTableView();
			initProductsTableView();
			initMessagesTableView();
		} catch (SQLException e) {
			errorMsg(e.getMessage());
		}
		

		allListeners.get(0).setManagerViewActiveStatus(true);
		((Stage)vbManagerRoot.getScene().getWindow()).setOnCloseRequest(e -> allListeners.get(0).setManagerViewActiveStatus(false));
 
	}

	public void registerListener(iViewManagerListenable l) {
		if (allListeners == null) {
			allListeners = new ArrayList<iViewManagerListenable>();
		}
		allListeners.add(l);
	}

	public void removeListener(iViewManagerListenable l) {
		if (allListeners == null) {
			return;
		}
		allListeners.remove(l);
	}


	
	public void errorMsg(String msg) {
		System.out.println(msg);
		JOptionPane.showMessageDialog(null, msg, "Error!", JOptionPane.ERROR_MESSAGE);
	}

	private void initCmbHours() throws SQLException {
		ArrayList<LocalTime> allHours = getAllOptionsForHours();
		allHours.stream().forEachOrdered(hourOption -> cmbOpenSunday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbOpenMonday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbOpenTuesday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbOpenWednesday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbOpenThursday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbOpenFriday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbOpenSaturday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbCloseSunday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbCloseMonday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbCloseTuesday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbCloseWednesday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbCloseThursday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbCloseFriday.getItems().add(hourOption));
		allHours.stream().forEachOrdered(hourOption -> cmbCloseSaturday.getItems().add(hourOption));

		initCmbHoursDetails();
	}

	private void initStyle() throws SQLException {
		initCmbStylePrice();
		initCmbStyleLength();
		initStyleCmbsDetails();
	}

	private void initCmbStylePrice() {
		ArrayList<Integer> allPrices = getAllOptionsForStylePrice();
		allPrices.stream().forEachOrdered(priceOption -> cmbPriceBeard.getItems().add(priceOption));
		allPrices.stream().forEachOrdered(priceOption -> cmbPriceChild.getItems().add(priceOption));
		allPrices.stream().forEachOrdered(priceOption -> cmbPriceColor.getItems().add(priceOption));
		allPrices.stream().forEachOrdered(priceOption -> cmbPriceEdges.getItems().add(priceOption));
		allPrices.stream().forEachOrdered(priceOption -> cmbPriceMan.getItems().add(priceOption));
		allPrices.stream().forEachOrdered(priceOption -> cmbPriceShades.getItems().add(priceOption));
		allPrices.stream().forEachOrdered(priceOption -> cmbPriceStraight.getItems().add(priceOption));
	}

	private void initCmbStyleLength() {
		ArrayList<Integer> allLengths = getAllOptionsForLength();
		allLengths.stream().forEachOrdered(lengthOption -> cmbLengthBeard.getItems().add(lengthOption));
		allLengths.stream().forEachOrdered(lengthOption -> cmbLengthChild.getItems().add(lengthOption));
		allLengths.stream().forEachOrdered(lengthOption -> cmbLengthColor.getItems().add(lengthOption));
		allLengths.stream().forEachOrdered(lengthOption -> cmbLengthEdges.getItems().add(lengthOption));
		allLengths.stream().forEachOrdered(lengthOption -> cmbLengthMan.getItems().add(lengthOption));
		allLengths.stream().forEachOrdered(lengthOption -> cmbLengthShades.getItems().add(lengthOption));
		allLengths.stream().forEachOrdered(lengthOption -> cmbLengthStraight.getItems().add(lengthOption));
	}

	private void initCmbHoursDetails() throws SQLException {
		ArrayList<BusinessDay> days = ActivityHoursSingleton.getInstance().getAllBusinessDays();

		if (days.get(0).isAvailable()) {
			cmbOpenSunday.setValue(days.get(0).getOpenHour());
			cmbCloseSunday.setValue(days.get(0).getCloseHour());
		} else {
			ckb1.setSelected(true);
			ckbCloseDay1();
		}

		if (days.get(1).isAvailable()) {
			cmbOpenMonday.setValue(days.get(1).getOpenHour());
			cmbCloseMonday.setValue(days.get(1).getCloseHour());
		} else {
			ckb2.setSelected(true);
			ckbCloseDay2();
		}

		if (days.get(2).isAvailable()) {
			cmbOpenTuesday.setValue(days.get(2).getOpenHour());
			cmbCloseTuesday.setValue(days.get(2).getCloseHour());
		} else {
			ckb3.setSelected(true);
			ckbCloseDay3();
		}

		if (days.get(3).isAvailable()) {
			cmbOpenWednesday.setValue(days.get(3).getOpenHour());
			cmbCloseWednesday.setValue(days.get(3).getCloseHour());
		} else {
			ckb4.setSelected(true);
			ckbCloseDay4();
		}

		if (days.get(4).isAvailable()) {
			cmbOpenThursday.setValue(days.get(4).getOpenHour());
			cmbCloseThursday.setValue(days.get(4).getCloseHour());
		} else {
			ckb5.setSelected(true);
			ckbCloseDay5();
		}

		if (days.get(5).isAvailable()) {
			cmbOpenFriday.setValue(days.get(5).getOpenHour());
			cmbCloseFriday.setValue(days.get(5).getCloseHour());
		} else {
			ckb6.setSelected(true);
			ckbCloseDay6();
		}

		if (days.get(6).isAvailable()) {
			cmbOpenSaturday.setValue(days.get(6).getOpenHour());
			cmbCloseSaturday.setValue(days.get(6).getCloseHour());
		} else {
			ckb7.setSelected(true);
			ckbCloseDay7();
		}
	}

	private void initStyleCmbsDetails() throws SQLException {
		StyleType temp = CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eBeard);
		cmbPriceBeard.setValue(temp.getPrice());
		cmbLengthBeard.setValue(temp.getLenght());
		temp = CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eMan);
		cmbPriceMan.setValue(temp.getPrice());
		cmbLengthMan.setValue(temp.getLenght());
		temp = CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eChild);
		cmbPriceChild.setValue(temp.getPrice());
		cmbLengthChild.setValue(temp.getLenght());
		temp = CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eStraight);
		cmbPriceStraight.setValue(temp.getPrice());
		cmbLengthStraight.setValue(temp.getLenght());
		temp = CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eEdges);
		cmbPriceEdges.setValue(temp.getPrice());
		cmbLengthEdges.setValue(temp.getLenght());
		temp = CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eColor);
		cmbPriceColor.setValue(temp.getPrice());
		cmbLengthColor.setValue(temp.getLenght());
		temp = CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eShades);
		cmbPriceShades.setValue(temp.getPrice());
		cmbLengthShades.setValue(temp.getLenght());
	}

	public void initMessagesTableView() throws SQLException {
		JFXTreeTableColumn<Message, String> time = new JFXTreeTableColumn<>("Time");
		time.setMinWidth(100);
		time.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Message, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getDateTimeString());
					}
				});

		JFXTreeTableColumn<Message, String> customer = new JFXTreeTableColumn<>("Customer");
		customer.setMinWidth(150);
		customer.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Message, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getCust().stringName());
					}
				});

		JFXTreeTableColumn<Message, String> phoneNumber = new JFXTreeTableColumn<>("Phone Number");
		phoneNumber.setMinWidth(150);
		phoneNumber.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Message, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getCust().getPhoneNumber());
					}
				});

		JFXTreeTableColumn<Message, String> msgSubject = new JFXTreeTableColumn<>("Message subject");
		msgSubject.setMinWidth(500);
		msgSubject.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Message, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getMsgSubject());
					}
				});

		ObservableList<Message> msgs = null;
		msgs = FXCollections.observableArrayList(ActivityHoursSingleton.getInstance().getMsgs());

		time.setStyle("-fx-alignment: CENTER;");
		customer.setStyle("-fx-alignment: CENTER;");
		phoneNumber.setStyle("-fx-alignment: CENTER;");

		final TreeItem<Message> root = new RecursiveTreeItem<Message>(msgs, RecursiveTreeObject::getChildren);
		tvManagerMassages.getColumns().setAll(time, customer, phoneNumber, msgSubject);
		tvManagerMassages.setRoot(root);
		tvManagerMassages.setShowRoot(false);
	}

	public void initAppointmentsTableView() {
		JFXTreeTableColumn<Customer, String> customer = new JFXTreeTableColumn<>("Customer");
		customer.setPrefWidth(80);
		customer.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Customer, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().stringName());
					}
				});

		JFXTreeTableColumn<Customer, String> time = new JFXTreeTableColumn<>("Time");
		time.setPrefWidth(70);
		time.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Customer, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getNextAppointment()
								.getTimeOfAppointment().format(DateTimeFormatter.ofPattern("HH:mm")) + "");
					}
				});

		JFXTreeTableColumn<Customer, String> treatment = new JFXTreeTableColumn<>("Treatment");
		treatment.setPrefWidth(90);
		treatment.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Customer, String> param) {
						return new SimpleStringProperty(
								param.getValue().getValue().getNextAppointment().getChoosenStyle().getAllStylesStrings());
					}
				});

		ObservableList<Customer> appointmentForToday = null;
		appointmentForToday = FXCollections.observableArrayList(
				allListeners.get(0).getActivityHoursMng().getAllCustomersInAppInDay(IsraelDayOfWeek.getToday()));

		customer.setStyle("-fx-alignment: CENTER;");
		time.setStyle("-fx-alignment: CENTER;");
		treatment.setStyle("-fx-alignment: CENTER;");

		final TreeItem<Customer> root = new RecursiveTreeItem<Customer>(appointmentForToday,
				RecursiveTreeObject::getChildren);
		tvManagerApp.getColumns().setAll(customer, time, treatment);
		tvManagerApp.setRoot(root);
		tvManagerApp.setShowRoot(false);
	}

	public void initAppointmentsNextDayTableView() {
		JFXTreeTableColumn<Customer, String> customer = new JFXTreeTableColumn<>("Customer");
		customer.setPrefWidth(80);
		customer.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Customer, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().stringName());
					}
				});

		JFXTreeTableColumn<Customer, String> time = new JFXTreeTableColumn<>("Time");
		time.setPrefWidth(70);
		time.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Customer, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getNextAppointment()
								.getTimeOfAppointment().format(DateTimeFormatter.ofPattern("HH:mm")) + "");
					}
				});

		JFXTreeTableColumn<Customer, String> treatment = new JFXTreeTableColumn<>("Treatment");
		treatment.setPrefWidth(90);
		treatment.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Customer, String> param) {
						return new SimpleStringProperty(
								param.getValue().getValue().getNextAppointment().getChoosenStyle().getAllStylesStrings());
					}
				});

		ObservableList<Customer> appointmentForToday = null;
		try {
			appointmentForToday = FXCollections.observableArrayList(
					ActivityHoursSingleton.getInstance().getAllCustomersInAppInDay(IsraelDayOfWeek.getTomorrow()));
		} catch (SQLException e) {
			errorMsg(e.getMessage());
		}

		customer.setStyle("-fx-alignment: CENTER;");
		time.setStyle("-fx-alignment: CENTER;");
		treatment.setStyle("-fx-alignment: CENTER;");

		final TreeItem<Customer> root = new RecursiveTreeItem<Customer>(appointmentForToday,
				RecursiveTreeObject::getChildren);
		tvManagerAppNextDay.getColumns().setAll(customer, time, treatment);
		tvManagerAppNextDay.setRoot(root);
		tvManagerAppNextDay.setShowRoot(false);
	}

	public void initProductsTableView() throws SQLException {
		visibilityProdChange(false);

		JFXTreeTableColumn<Product, String> id = new JFXTreeTableColumn<>("ID");
		id.setPrefWidth(40);

		id.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Product, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getId() + "");
					}
				});

		JFXTreeTableColumn<Product, String> name = new JFXTreeTableColumn<>("Name");
		name.setPrefWidth(130);
		name.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Product, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getName());
					}
				});

		JFXTreeTableColumn<Product, String> price = new JFXTreeTableColumn<>("Price");
		price.setPrefWidth(60);
		price.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Product, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getPrice() + "");
					}
				});

		JFXTreeTableColumn<Product, HBox> button = new JFXTreeTableColumn<>("");
		button.setPrefWidth(80);
		button.setMinWidth(80);
		button.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, HBox>, ObservableValue<HBox>>() {

					@Override
					public ObservableValue<HBox> call(TreeTableColumn.CellDataFeatures<Product, HBox> currentRow) {
						JFXButton removeButten = new JFXButton("Remove");

						removeButten.setOnAction(e -> {

							try {
								BarberMngSingleton.getInstance().removeProductFromDB(currentRow.getValue().getValue());
								initProductsTableView();
							} catch (SQLException e1) {
								errorMsg(e1.getMessage());
							}
						});

						ObservableValue<HBox> s = new SimpleObjectProperty<>(new HBox(removeButten));
						return s;
					}
				});

		ObservableList<Product> products = null;
		products = FXCollections.observableArrayList(BarberMngSingleton.getInstance().getAllProducts());

		id.setStyle("-fx-alignment: CENTER;");
		name.setStyle("-fx-alignment: CENTER;");
		price.setStyle("-fx-alignment: CENTER;");

		final TreeItem<Product> root = new RecursiveTreeItem<Product>(products, RecursiveTreeObject::getChildren);

		tvProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				lbProdId.setText(newSelection.getValue().getId() + "");
				tfProdName.setText(newSelection.getValue().getName());
				ArrayList<Integer> allProdPrices = getAllOptionsForProductsPrice();
				allProdPrices.stream().forEachOrdered(priceOption -> cmbProdPrice.getItems().add(priceOption));
				cmbProdPrice.setValue(newSelection.getValue().getPrice());
				btnAddProduct.setDisable(false);
				visibilityProdChange(true);
			} else
				visibilityProdChange(false);
		});

		tvProducts.setEditable(true);
		tvProducts.getColumns().setAll(id, name, price, button);
		tvProducts.setRoot(root);
		tvProducts.setShowRoot(false);
	}

	private ArrayList<LocalTime> getAllOptionsForHours() {
		LocalTime timeOfStart = LocalTime.parse("07:00");
		LocalTime timeOfClose = LocalTime.parse("22:00");
		ArrayList<LocalTime> allOptionsForHours = new ArrayList<LocalTime>();
		while (!timeOfStart.isAfter(timeOfClose)) {
			allOptionsForHours.add(timeOfStart);
			timeOfStart = timeOfStart.plusMinutes(30);
		}
		return allOptionsForHours;
	}

	private ArrayList<Integer> getAllOptionsForStylePrice() {
		ArrayList<Integer> allOptionsForStylePrice = new ArrayList<Integer>();
		for (int i = 20; i <= 500; i += 10)
			allOptionsForStylePrice.add(i);
		return allOptionsForStylePrice;
	}

	private ArrayList<Integer> getAllOptionsForLength() {
		ArrayList<Integer> allOptionsForLength = new ArrayList<Integer>();
		for (int i = 10; i <= 240; i += 10)
			allOptionsForLength.add(i);
		return allOptionsForLength;
	}

	private ArrayList<Integer> getAllOptionsForProductsPrice() {
		ArrayList<Integer> allOptionsForProductsPrice = new ArrayList<Integer>();
		for (int i = 10; i <= 1000; i += 10)
			allOptionsForProductsPrice.add(i);
		return allOptionsForProductsPrice;
	}

	@FXML
	void btnUpdateActivityHours() {
		try {
			ActivityHoursSingleton.getInstance().updateActivityHoursInDB(IsraelDayOfWeek.SUNDAY,
					cmbOpenSunday.getValue(), cmbCloseSunday.getValue());
			ActivityHoursSingleton.getInstance().updateActivityHoursInDB(IsraelDayOfWeek.MONDAY,
					cmbOpenMonday.getValue(), cmbCloseMonday.getValue());
			ActivityHoursSingleton.getInstance().updateActivityHoursInDB(IsraelDayOfWeek.TUESDAY,
					cmbOpenTuesday.getValue(), cmbCloseTuesday.getValue());
			ActivityHoursSingleton.getInstance().updateActivityHoursInDB(IsraelDayOfWeek.WEDNESDAY,
					cmbOpenWednesday.getValue(), cmbCloseWednesday.getValue());
			ActivityHoursSingleton.getInstance().updateActivityHoursInDB(IsraelDayOfWeek.THURSDAY,
					cmbOpenThursday.getValue(), cmbCloseThursday.getValue());
			ActivityHoursSingleton.getInstance().updateActivityHoursInDB(IsraelDayOfWeek.FRIDAY,
					cmbOpenFriday.getValue(), cmbCloseFriday.getValue());
			ActivityHoursSingleton.getInstance().updateActivityHoursInDB(IsraelDayOfWeek.SATURDAY,
					cmbOpenSaturday.getValue(), cmbCloseSaturday.getValue());

			initCmbHours();
			ActivityHoursSingleton.getInstance().notifyCustomerObservers("Activity hours has changed, new hours: ");
			allListeners.get(0).activityHoursChanged("Activity hours has changed");
			initMessagesTableView();
		} catch (SQLException e) {
			errorMsg(e.getMessage());
		}
	}

	@FXML
	void btnUpdateStyles() {
		try {
			CatalogStylesSingleton.getInstance().updateStyleInDB(eStyleTypes.eBeard, cmbLengthBeard.getValue(),
					cmbPriceBeard.getValue());
			CatalogStylesSingleton.getInstance().updateStyleInDB(eStyleTypes.eMan, cmbLengthMan.getValue(),
					cmbPriceMan.getValue());
			CatalogStylesSingleton.getInstance().updateStyleInDB(eStyleTypes.eChild, cmbLengthChild.getValue(),
					cmbPriceChild.getValue());
			CatalogStylesSingleton.getInstance().updateStyleInDB(eStyleTypes.eColor, cmbLengthColor.getValue(),
					cmbPriceColor.getValue());
			CatalogStylesSingleton.getInstance().updateStyleInDB(eStyleTypes.eEdges, cmbLengthEdges.getValue(),
					cmbPriceEdges.getValue());
			CatalogStylesSingleton.getInstance().updateStyleInDB(eStyleTypes.eShades, cmbLengthShades.getValue(),
					cmbPriceShades.getValue());
			CatalogStylesSingleton.getInstance().updateStyleInDB(eStyleTypes.eStraight, cmbLengthStraight.getValue(),
					cmbPriceStraight.getValue());
		} catch (SQLException e) {
			errorMsg(e.getMessage());
		}
	}

	@FXML
	void btnShowPlaceToAddProduct() {
		btnAddProduct.setDisable(true);
		lbProdId.setText("Name:  ");
		ArrayList<Integer> allProdPrices = getAllOptionsForProductsPrice();
		allProdPrices.stream().forEachOrdered(priceOption -> cmbProdPrice.getItems().add(priceOption));
		tfProdName.setText(null);
		cmbProdPrice.setValue(null);
		visibilityProdAdd(true);
	}

	@FXML
	void btnSaveChangesInProducts() {
		Product theProduct;
		try {
			theProduct = BarberMngSingleton.getInstance().getProductByID(Integer.parseInt(lbProdId.getText()));
			theProduct.setName(tfProdName.getText());
			theProduct.setPrice(cmbProdPrice.getValue());
			BarberMngSingleton.getInstance().updateProductDetailsInDB(theProduct);
			initProductsTableView();
		} catch (NumberFormatException | SQLException e) {
			errorMsg(e.getMessage());
		}
	}

	@FXML
	void btnAddNewProduct() {
		Product newProduct;
		try {
			if (tfProdName.getText().isEmpty() || cmbProdPrice.getValue() == null) {
				errorMsg("All fields must be selected");
			} else {
				newProduct = new Product(tfProdName.getText(), cmbProdPrice.getValue());
				BarberMngSingleton.getInstance().addProductToDB(newProduct);
				initProductsTableView();
				visibilityProdAdd(false);
				btnAddProduct.setDisable(false);
			}
		} catch (NumberFormatException | SQLException e) {
			errorMsg(e.getMessage());
		}
	}

	@FXML
	void ckbCloseDay1() {
		LocalTime closeDay = LocalTime.parse("00:00");

		if (ckb1.isSelected()) {
			cmbOpenSunday.getItems().add(closeDay);
			cmbCloseSunday.getItems().add(closeDay);
			cmbOpenSunday.setValue(closeDay);
			cmbCloseSunday.setValue(closeDay);
			cmbOpenSunday.setDisable(true);
			cmbCloseSunday.setDisable(true);
		} else if (cmbOpenSunday.isDisable()) {
			cmbOpenSunday.getItems().remove(closeDay);
			cmbCloseSunday.getItems().remove(closeDay);
			cmbOpenSunday.setValue(null);
			cmbCloseSunday.setValue(null);
			cmbOpenSunday.setDisable(false);
			cmbCloseSunday.setDisable(false);
		}
	}

	@FXML
	void ckbCloseDay2() {
		LocalTime closeDay = LocalTime.parse("00:00");

		if (ckb2.isSelected()) {
			cmbOpenMonday.getItems().add(closeDay);
			cmbCloseMonday.getItems().add(closeDay);
			cmbOpenMonday.setValue(closeDay);
			cmbCloseMonday.setValue(closeDay);
			cmbOpenMonday.setDisable(true);
			cmbCloseMonday.setDisable(true);
		} else if (cmbOpenMonday.isDisable()) {
			cmbOpenMonday.getItems().remove(closeDay);
			cmbCloseMonday.getItems().remove(closeDay);
			cmbOpenMonday.setValue(null);
			cmbCloseMonday.setValue(null);
			cmbOpenMonday.setDisable(false);
			cmbCloseMonday.setDisable(false);
		}
	}

	@FXML
	void ckbCloseDay3() {
		LocalTime closeDay = LocalTime.parse("00:00");

		if (ckb3.isSelected()) {
			cmbOpenTuesday.getItems().add(closeDay);
			cmbCloseTuesday.getItems().add(closeDay);
			cmbOpenTuesday.setValue(closeDay);
			cmbCloseTuesday.setValue(closeDay);
			cmbOpenTuesday.setDisable(true);
			cmbCloseTuesday.setDisable(true);
		} else if (cmbOpenTuesday.isDisable()) {
			cmbOpenTuesday.getItems().remove(closeDay);
			cmbCloseTuesday.getItems().remove(closeDay);
			cmbOpenTuesday.setValue(null);
			cmbCloseTuesday.setValue(null);
			cmbOpenTuesday.setDisable(false);
			cmbCloseTuesday.setDisable(false);
		}
	}

	@FXML
	void ckbCloseDay4() {
		LocalTime closeDay = LocalTime.parse("00:00");

		if (ckb4.isSelected()) {
			cmbOpenWednesday.getItems().add(closeDay);
			cmbCloseWednesday.getItems().add(closeDay);
			cmbOpenWednesday.setValue(closeDay);
			cmbCloseWednesday.setValue(closeDay);
			cmbOpenWednesday.setDisable(true);
			cmbCloseWednesday.setDisable(true);
		} else if (cmbOpenWednesday.isDisable()) {
			cmbOpenWednesday.getItems().remove(closeDay);
			cmbCloseWednesday.getItems().remove(closeDay);
			cmbOpenWednesday.setValue(null);
			cmbCloseWednesday.setValue(null);
			cmbOpenWednesday.setDisable(false);
			cmbCloseWednesday.setDisable(false);
		}
	}

	@FXML
	void ckbCloseDay5() {
		LocalTime closeDay = LocalTime.parse("00:00");

		if (ckb5.isSelected()) {
			cmbOpenThursday.getItems().add(closeDay);
			cmbCloseThursday.getItems().add(closeDay);
			cmbOpenThursday.setValue(closeDay);
			cmbCloseThursday.setValue(closeDay);
			cmbOpenThursday.setDisable(true);
			cmbCloseThursday.setDisable(true);
		} else if (cmbOpenThursday.isDisable()) {
			cmbOpenThursday.getItems().remove(closeDay);
			cmbCloseThursday.getItems().remove(closeDay);
			cmbOpenThursday.setValue(null);
			cmbCloseThursday.setValue(null);
			cmbOpenThursday.setDisable(false);
			cmbCloseThursday.setDisable(false);
		}
	}

	@FXML
	void ckbCloseDay6() {
		LocalTime closeDay = LocalTime.parse("00:00");

		if (ckb6.isSelected()) {
			cmbOpenFriday.getItems().add(closeDay);
			cmbCloseFriday.getItems().add(closeDay);
			cmbOpenFriday.setValue(closeDay);
			cmbCloseFriday.setValue(closeDay);
			cmbOpenFriday.setDisable(true);
			cmbCloseFriday.setDisable(true);
		} else if (cmbOpenFriday.isDisable()) {
			cmbOpenFriday.getItems().remove(closeDay);
			cmbCloseFriday.getItems().remove(closeDay);
			cmbOpenFriday.setValue(null);
			cmbCloseFriday.setValue(null);
			cmbOpenFriday.setDisable(false);
			cmbCloseFriday.setDisable(false);
		}
	}

	@FXML
	void ckbCloseDay7() {
		LocalTime closeDay = LocalTime.parse("00:00");

		if (ckb7.isSelected()) {
			cmbOpenSaturday.getItems().add(closeDay);
			cmbCloseSaturday.getItems().add(closeDay);
			cmbOpenSaturday.setValue(closeDay);
			cmbCloseSaturday.setValue(closeDay);
			cmbOpenSaturday.setDisable(true);
			cmbCloseSaturday.setDisable(true);
		} else if (cmbOpenSaturday.isDisable()) {
			cmbOpenSaturday.getItems().remove(closeDay);
			cmbCloseSaturday.getItems().remove(closeDay);
			cmbOpenSaturday.setValue(null);
			cmbCloseSaturday.setValue(null);
			cmbOpenSaturday.setDisable(false);
			cmbCloseSaturday.setDisable(false);
		}
	}

	private void visibilityProdChange(Boolean change) {
		lbProdId.setVisible(change);
		tfProdName.setVisible(change);
		cmbProdPrice.setVisible(change);
		btnSaveProd.setVisible(change);
		btnAddProd.setVisible(false);
	}

	private void visibilityProdAdd(Boolean change) {
		lbProdId.setVisible(change);
		tfProdName.setVisible(change);
		cmbProdPrice.setVisible(change);
		btnSaveProd.setVisible(false);
		btnAddProd.setVisible(change);
	}

}