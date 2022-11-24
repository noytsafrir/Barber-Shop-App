package view.Pages.Appointment;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.classes.*;
import model.classes.Product;
import view.Pages.iCustomerViewChild;
import view.Pages.RootController.ViewRoot;

public class ViewAppProducts implements iCustomerViewChild {

	private ViewRoot parentController;
	private HashMap<Product, Integer> productsCart;
	private HashSet<Product> allProducts;
	private Customer cust;

	@FXML
	private JFXButton btnBack;
	@FXML
	private JFXTreeTableView<Product> tvShopCart;
	@FXML
	private JFXTreeTableView<Product> tvProducts;
	@FXML
	private Label lblTotalPrice;

	@Override
	public void initializePage() {
		cust = parentController.getUserCustomer();
		if(cust == null) {
			parentController.moveToStartPage();;
			return;
		}
		
		initCustomerProductsCart();
		
		initProductsList();
		if(allProducts == null) {
			parentController.moveToUserHomePage();
			return;
		}
			
		initShopCartTableView();
		initProductsTableView();
		initTotalPrice();
		btnBack.setFocusTraversable(false);

	}

	@Override
	public void setParentController(ViewRoot parent) {
		parentController = parent;
	}

	private void initCustomerProductsCart() {
		if (parentController.isLoggedIn())
			if (parentController.getUserCustomer().isNextAppointment())
				productsCart = parentController.getUserCustomer().getNextAppointment().getProductsCart();
	}

	private void initTotalPrice() {
		lblTotalPrice.setText(cust.getNextAppointment().sumProductsCost() + " ¤");
	}

	private void initProductsList() {
		allProducts = parentController.getMvcController().getAllProducts();
	}

	public void initShopCartTableView() {
		JFXTreeTableColumn<Product, String> id = new JFXTreeTableColumn<>("ID");
		id.setPrefWidth(60);

		id.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Product, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getId() + "");
					}
				});

		JFXTreeTableColumn<Product, String> name = new JFXTreeTableColumn<>("Name");
		name.setPrefWidth(110);
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

		JFXTreeTableColumn<Product, HBox> cnt = new JFXTreeTableColumn<>("Count");
		cnt.setPrefWidth(70);
		cnt.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Product, HBox>, ObservableValue<HBox>>() {

			@Override
			public ObservableValue<HBox> call(TreeTableColumn.CellDataFeatures<Product, HBox> currentRow) {
				Spinner<Integer> count = new Spinner<Integer>();
				count.setPrefWidth(55);
				SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10,
						productsCart.get(currentRow.getValue().getValue()));
				count.setValueFactory(valueFactory);
				count.valueProperty().addListener(new ChangeListener<Integer>() {
					@Override
					public void changed(ObservableValue<? extends Integer> observable, Integer oldValue,
							Integer newValue) {
						if (newValue > 0) {
							try {
								cust.getNextAppointment().addProductToCart(currentRow.getValue().getValue(), newValue);
							} catch (SQLException e) {
								parentController.errorMsg(e.getMessage());
								productsCart.put(currentRow.getValue().getValue(), oldValue);
								initShopCartTableView();
							}
						} else {
							try {
								cust.getNextAppointment().removeProductFromCart(currentRow.getValue().getValue());
							} catch (SQLException e) {
								parentController.errorMsg(e.getMessage());
								productsCart.put(currentRow.getValue().getValue(), oldValue);
							}
							initShopCartTableView();
						}
						initTotalPrice();
					}
				});
				ObservableValue<HBox> hbCount = new SimpleObjectProperty<>(new HBox(count));
				return hbCount;
			}
		});

		ObservableList<Product> products = null;
		products = FXCollections.observableArrayList(productsCart.keySet());

		final TreeItem<Product> root = new RecursiveTreeItem<Product>(products, RecursiveTreeObject::getChildren);
		tvShopCart.getColumns().setAll(id, name, price, cnt);
		tvShopCart.setRoot(root);
		tvShopCart.setShowRoot(false);

	}

	public void initProductsTableView() {
		JFXTreeTableColumn<Product, String> id = new JFXTreeTableColumn<>("ID");
		id.setPrefWidth(60);

		id.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Product, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getId() + "");
					}
				});

		JFXTreeTableColumn<Product, String> name = new JFXTreeTableColumn<>("Name");
		name.setPrefWidth(110);
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

		JFXTreeTableColumn<Product, HBox> btnAdd = new JFXTreeTableColumn<>();
		btnAdd.setPrefWidth(70);
		btnAdd.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, HBox>, ObservableValue<HBox>>() {

					@Override
					public ObservableValue<HBox> call(TreeTableColumn.CellDataFeatures<Product, HBox> currentRow) {
						JFXButton btn = new JFXButton("Add");

						btn.setOnAction(e -> {
							if (!productsCart.containsKey(currentRow.getValue().getValue())) {
								try {
									cust.getNextAppointment().addProductToCart(currentRow.getValue().getValue(), 1);
								} catch (SQLException e1) {
									parentController.errorMsg(e1.getMessage());
									productsCart.remove(currentRow.getValue().getValue());
									initShopCartTableView();
								}
							}
							initShopCartTableView();
							initTotalPrice();
						});

						ObservableValue<HBox> hbBtn = new SimpleObjectProperty<>(new HBox(btn));
						return hbBtn;
					}
				});

		ObservableList<Product> products = null;
		products = FXCollections.observableArrayList(allProducts);

		final TreeItem<Product> root = new RecursiveTreeItem<Product>(products, RecursiveTreeObject::getChildren);
		tvProducts.getColumns().setAll(id, name, price, btnAdd);
		tvProducts.setRoot(root);
		tvProducts.setShowRoot(false);
	}

	@FXML
	void btnActionBack() {
		parentController.moveToUserHomePage();
	}
}