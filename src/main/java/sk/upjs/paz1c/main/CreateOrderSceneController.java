package sk.upjs.paz1c.main;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;
import sk.upjs.paz1c.storage.Order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

public class CreateOrderSceneController {

	@FXML
	private Button addButton;

	@FXML
	private DatePicker dayDatePicker;

	@FXML
	private Button deleteButton;

	@FXML
	private ComboBox<Food> foodComboBox;

	@FXML
	private Spinner<Integer> portionsSpinner;

	@FXML
	private TableView<?> portionsTableView;

	private OrderFxModel orderModel;
	private FoodDao foodDao = DaoFactory.INSTANCE.getFoodDao();
	private Food selectedFood;
	private Integer selectedPortions;

	public CreateOrderSceneController(Order selectedOrder) {
		orderModel = new OrderFxModel(selectedOrder);
	}

	public CreateOrderSceneController() {
		orderModel = new OrderFxModel();
	}

	@FXML
	void initialize() {
		foodComboBox.setItems(FXCollections.observableArrayList(foodDao.getAll()));
		foodComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Food>() {

			@Override
			public void changed(ObservableValue<? extends Food> observable, Food oldValue, Food newValue) {
				selectedFood = newValue;

			}
		});

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
				Integer.MAX_VALUE, 0);
		portionsSpinner.setValueFactory(valueFactory);
		portionsSpinner.getValueFactory().valueProperty().addListener(new ChangeListener<Integer>() {

			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				selectedPortions = newValue;
			}
		});

		// https://stackoverflow.com/questions/20383773/set-date-picker-time-format/21498568
		dayDatePicker.setConverter(new StringConverter<LocalDate>() {
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
		
		dayDatePicker.valueProperty().bindBidirectional(orderModel.dayProperty());

	}

	@FXML
	void addFood(ActionEvent event) {
		for (Food f : orderModel.getPortions().keySet()) {
			if (f.getId().equals(selectedFood.getId())) {
				orderModel.getPortions().put(f, selectedPortions);
			}
		}

		// add line to table
	}

	@FXML
	void cancelChanges(ActionEvent event) {
		foodComboBox.getScene().getWindow().hide();
	}

	@FXML
	void deleteFood(ActionEvent event) {

	}

	@FXML
	void saveChanges(ActionEvent event) {

	}

}
