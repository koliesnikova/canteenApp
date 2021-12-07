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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
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

		// TODO
		datePicker.setConverter(new StringConverter<LocalDate>() {
			
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
			
			@Override
			public String toString(LocalDate object) {
				if(object == null)
		            return "";
		        return dateTimeFormatter.format(object);
			}
			
			@Override
			public LocalDate fromString(String string) {
				if(string == null || string.trim().isEmpty())
		            return null;
		        
		        return LocalDate.parse(string,dateTimeFormatter);
			}
		});
		
		datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			LocalDateTime ldt = LocalDateTime.of(newValue, LocalTime.of(0, 0, 0));
			toBuyTable.setItems(FXCollections.observableArrayList(defaultManager.updateShoppingList(orderDao.getByDay(ldt))));
		});
		
		ingredientCol.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
		//toBuyTable.getColumns().add(ingredientCol);
		
		amountCol.setCellValueFactory(new PropertyValueFactory<>("toBuy"));
		//toBuyTable.getColumns().add(amountCol);
		if(datePicker.getValue() == null) {
			List<Order> notPreparedOrds = orderDao.getByPrepared(false);
			for (Order order : notPreparedOrds) {
				toBuyTable.setItems(FXCollections.observableArrayList(defaultManager.updateShoppingList(order)));
			}
		}
		

	}

}
