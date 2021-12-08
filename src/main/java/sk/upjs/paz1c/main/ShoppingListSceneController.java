package sk.upjs.paz1c.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import sk.upjs.paz1c.biznis.DefaultCanteenManager;
import sk.upjs.paz1c.biznis.ShoppingListItemOverview;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.Ingredient;
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

	private DefaultCanteenManager defaultManager = new DefaultCanteenManager();
	private OrderDao orderDao = DaoFactory.INSTANCE.getOrderDao();

	@FXML
	void initialize() {
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
				toBuyTable.setItems(FXCollections
						.observableArrayList(defaultManager.getItemsForShoppingList(orderDao.getByPrepared(false))));
			} else {
				LocalDateTime ldt = LocalDateTime.of(newValue, LocalTime.of(0, 0, 0));
				if (orderDao.getByDay(ldt) != null) {
					toBuyTable.getItems().clear();
					toBuyTable.setItems(FXCollections.observableArrayList(defaultManager.getItemsForShoppingList(ldt)));
				} else {
					// TODO alert - no orders for the day
					System.out.println("no orders for that day");
				}
			}
		});

		ingredientCol.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("toBuy"));

		if (datePicker.getValue() == null) {
			// show items for each day
			toBuyTable.setItems(FXCollections
					.observableArrayList(defaultManager.getItemsForShoppingList(orderDao.getByPrepared(false))));

		}

	}

}
