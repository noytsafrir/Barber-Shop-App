package view.Pages.CatalogStyles;

import java.util.HashSet;

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
import model.classes.Product;
import model.classes.StyleType;
import model.singletons.CatalogStylesSingleton;
import view.Pages.iCustomerViewChild;
import view.Pages.RootController.ViewRoot;

public class ViewCatalogStyles implements iCustomerViewChild {

	private ViewRoot parentController;
	private CatalogStylesSingleton catalogMng;
	private HashSet<Product> allProducts;

	@FXML
	private JFXTreeTableView<StyleType> treeView;
	@FXML
	private JFXTreeTableView<Product> tvProducts;

	@Override
	public void initializePage() {
		catalogMng = parentController.getMvcController().getCatalogMng();
		allProducts = parentController.getMvcController().getAllProducts();

		initStylesTableView();
		initProductsTableView();
	}

	public void initStylesTableView() {
		if (catalogMng == null)
			return;

		JFXTreeTableColumn<StyleType, String> name = new JFXTreeTableColumn<>("Style name");
		name.setMinWidth(120);
		name.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<StyleType, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StyleType, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getName());
					}
				});

		JFXTreeTableColumn<StyleType, String> price = new JFXTreeTableColumn<>("Price");
		price.setMinWidth(80);
		price.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<StyleType, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StyleType, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getPrice() + " ¤");
					}
				});

		JFXTreeTableColumn<StyleType, String> length = new JFXTreeTableColumn<>("Time lenght");
		length.setMinWidth(80);
		length.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<StyleType, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<StyleType, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getLenght() + " minutes");
					}
				});

		ObservableList<StyleType> styleTypes = null;
		styleTypes = FXCollections.observableArrayList(catalogMng.getAllStyles());

		name.setStyle("-fx-alignment: CENTER;");
		price.setStyle("-fx-alignment: CENTER;");
		length.setStyle("-fx-alignment: CENTER;");

		final TreeItem<StyleType> root = new RecursiveTreeItem<StyleType>(styleTypes, RecursiveTreeObject::getChildren);
		treeView.getColumns().setAll(name, price, length);
		treeView.setRoot(root);
		treeView.setShowRoot(false);
	}

	public void initProductsTableView() {
		if (allProducts == null)
			return;

		JFXTreeTableColumn<Product, String> id = new JFXTreeTableColumn<>("ID");
		id.setMinWidth(80);

		id.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Product, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getId() + "");
					}
				});

		JFXTreeTableColumn<Product, String> name = new JFXTreeTableColumn<>("Name");
		name.setMinWidth(130);
		name.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Product, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getName());
					}
				});

		JFXTreeTableColumn<Product, String> price = new JFXTreeTableColumn<>("Price");
		price.setMinWidth(90);
		price.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Product, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Product, String> param) {
						return new SimpleStringProperty(param.getValue().getValue().getPrice() + " ¤");
					}
				});

		ObservableList<Product> products = null;
		products = FXCollections.observableArrayList(allProducts);

		id.setStyle("-fx-alignment: CENTER;");
		name.setStyle("-fx-alignment: CENTER;");
		price.setStyle("-fx-alignment: CENTER;");

		final TreeItem<Product> root = new RecursiveTreeItem<Product>(products, RecursiveTreeObject::getChildren);
		tvProducts.getColumns().setAll(id, name, price);
		tvProducts.setRoot(root);
		tvProducts.setShowRoot(false);
	}

	@Override
	public void setParentController(ViewRoot parent) {
		parentController = parent;
	}

}