package sk.upjs.paz1c.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import sk.upjs.paz1c.biznis.DefaultCanteenManager;
import sk.upjs.paz1c.biznis.OrderFoodOverview;
import sk.upjs.paz1c.biznis.ShoppingListItemOverview;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;
import sk.upjs.paz1c.storage.Order;
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
				toBuyTable.setItems(FXCollections.observableArrayList(defaultManager.getAllToBuy()));
			} else {
				LocalDateTime ldt = LocalDateTime.of(newValue, LocalTime.of(0, 0, 0));
				if (orderDao.getByDay(ldt) != null && orderDao.getByDay(ldt).size() > 0) {
					toBuyTable.getItems().clear();
					System.out.println("by date: " + defaultManager.getItemsForShoppingList(ldt));
					toBuyTable.setItems(FXCollections.observableArrayList(defaultManager.getItemsForShoppingList(ldt)));
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("There are no orders.");
					alert.setContentText("For selected date (" + datePicker.getValue().getDayOfMonth() + "-"
							+ datePicker.getValue().getMonthValue() + "-" + datePicker.getValue().getYear()
							+ ") are registered no orders. Try another date. ");
					alert.show();
				}
			}
		});

		ingredientCol.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("toBuy"));

		if (datePicker.getValue() == null) {
			toBuyTable.setItems(FXCollections.observableArrayList(defaultManager.getAllToBuy()));
		}
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

		// TODO update table
	}
}
