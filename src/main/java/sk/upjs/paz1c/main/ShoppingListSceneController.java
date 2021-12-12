package sk.upjs.paz1c.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import sk.upjs.paz1c.biznis.DefaultCanteenManager;
import sk.upjs.paz1c.biznis.ShoppingListItemOverview;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;
import sk.upjs.paz1c.storage.OrderDao;

public class ShoppingListSceneController {

	@FXML
	private TableView<ShoppingListItemOverview> toBuyTable;

	@FXML
	private DatePicker datePicker;

	@FXML
	private TableColumn<ShoppingListItemOverview, String> ingredientCol;

	@FXML
	private TableColumn<ShoppingListItemOverview, String> amountCol;

	@FXML
	private Button boughtButton;
	
	@FXML
	private Button showAllButton;

	private DefaultCanteenManager defaultManager = new DefaultCanteenManager();
	private OrderDao orderDao = DaoFactory.INSTANCE.getOrderDao();
	private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
	private ShoppingListItemOverview selected = null;

	@FXML
	void initialize() {
		boughtButton.setDisable(true);
		datePicker.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");

			@Override
			public String toString(LocalDate object) {
				if (object == null)
					return "";
				return dateTimeFormatter.format(object);
			}

			@Override
			public LocalDate fromString(String string) {
				if (string == null || string.trim().isEmpty())
					return null;
				return LocalDate.parse(string, dateTimeFormatter);
			}
		});

		datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				updateTable(LocalDateTime.MAX);
			} else {
				LocalDateTime ldt = LocalDateTime.of(newValue, LocalTime.of(0, 0, 0));
				updateTable(ldt);
			}
		});

		ingredientCol.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("toBuy"));
		
		ingredientCol.prefWidthProperty().bind(toBuyTable.widthProperty().multiply(0.6));
		amountCol.prefWidthProperty().bind(toBuyTable.widthProperty().multiply(0.4));

		if (datePicker.getValue() == null) {
			showAllButton.setDisable(true);
			toBuyTable.setItems(FXCollections.observableArrayList(defaultManager.getAllToBuy()));
		}
		//https://stackoverflow.com/questions/24765549/remove-the-default-no-content-in-table-text-for-empty-javafx-table
		toBuyTable.setPlaceholder(new Label("All orders for selected day have been prepared. :-)"));
		toBuyTable.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<ShoppingListItemOverview>() {
					@Override
					public void changed(ObservableValue<? extends ShoppingListItemOverview> observable,
							ShoppingListItemOverview oldValue, ShoppingListItemOverview newValue) {
						selected = newValue;
						if (selected != null) {
							boughtButton.setDisable(false);
						}
					}
				});
	}

	@FXML
	void setAsBought() {
		Ingredient i = selected.getIngredient();
		i.setAmountAvailiable(selected.getToBuy() + i.getAmountAvailiable());
		ingredientDao.save(i);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Ingredient is bought.");
		alert.setContentText(selected.getIngredient().getName()
				+ " was removed from your shopping list and and it's amount avilable was refilled.");

		if (datePicker.getValue() == null) {
			updateTable(LocalDateTime.MAX);
		}else {
			LocalDateTime ldt = LocalDateTime.of(datePicker.getValue(), LocalTime.of(0, 0, 0));
			updateTable(ldt);
		}

	}

	@FXML
	void showAllIngredients(){
		datePicker.setValue(null);
		
	}
	void updateTable(LocalDateTime date) {
		if (date == LocalDateTime.MAX) {
			showAllButton.setDisable(true);
			toBuyTable.setItems(FXCollections.observableArrayList(defaultManager.getAllToBuy()));
		} else {
			if (orderDao.getByDay(date) != null && orderDao.getByDay(date).size() > 0) {
				showAllButton.setDisable(false);
				toBuyTable.getItems().clear();
				toBuyTable.setItems(FXCollections.observableArrayList(defaultManager.getItemsForShoppingList(date)));
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("There are no orders.");
				alert.setContentText("For selected date (" + date.getDayOfMonth() + "-" + date.getMonthValue() + "-"
						+ date.getYear() + ") are registered no orders. Try another date. ");
				alert.show();
			}
		}
	}
}
