package sk.upjs.paz1c.main;

import sk.upjs.paz1c.biznis.OrderFoodOverview;
import sk.upjs.paz1c.biznis.OverviewManager;
import sk.upjs.paz1c.biznis.OverviewManagerImpl;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;
import sk.upjs.paz1c.storage.Order;
import sk.upjs.paz1c.storage.OrderDao;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;


/*
 * 
 *  TO DO:
 *  ak sa len nejake jedlo ide upravovat aby v comboboxe neboli 
 *  tie jedla ktore uz su v tabulke zapisane 
 *  '-> najefektivnejsie asi tahat priamo z databazy len vhodne jedla
 * 
 */




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
	private TableView<OrderFoodOverview> portionsTableView;

	private OrderFxModel orderModel;
	private FoodDao foodDao = DaoFactory.INSTANCE.getFoodDao();
	private OrderDao orderDao = DaoFactory.INSTANCE.getOrderDao();
	private Food selectedFood;
	private Integer selectedPortions;
	private OverviewManager manager = new OverviewManagerImpl();
	private OrderFoodOverview selectedOverview;

	public CreateOrderSceneController(Order selectedOrder) {
		orderModel = new OrderFxModel(selectedOrder);
	}

	public CreateOrderSceneController() {
		orderModel = new OrderFxModel();
	}

	@FXML
	void initialize() {
		deleteButton.setDisable(true);
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

		TableColumn<OrderFoodOverview, String> nameCol = new TableColumn<OrderFoodOverview, String>("Food name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		portionsTableView.getColumns().add(nameCol);
		
		TableColumn<OrderFoodOverview, Integer> countCol = new TableColumn<OrderFoodOverview, Integer>("Portions");
		countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
		portionsTableView.getColumns().add(countCol);
		
		TableColumn<OrderFoodOverview, Double> totalSumCol = new TableColumn<OrderFoodOverview, Double>("Total price");
		totalSumCol.setCellValueFactory(new PropertyValueFactory<>("totalSum"));
		portionsTableView.getColumns().add(totalSumCol);
		
		portionsTableView.setItems(FXCollections.observableArrayList(manager.getAll(orderModel.getId())));
		portionsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderFoodOverview>() {

			@Override
			public void changed(ObservableValue<? extends OrderFoodOverview> observable, OrderFoodOverview oldValue,
					OrderFoodOverview newValue) {
				selectedOverview = newValue;
				deleteButton.setDisable(false);
				
			}
		});
	}//inicialize

	@FXML
	void addFood(ActionEvent event) {
		for (Food f : orderModel.getPortions().keySet()) {
			if (f.getId().equals(selectedFood.getId())) {
				orderModel.getPortions().put(f, selectedPortions);
				OrderFoodOverview o = new OrderFoodOverview(f.getId(), f.getName(), orderModel.getPortions().get(f), f.getPrice());
				portionsTableView.getItems().add(o);
				
				for (Food food : foodComboBox.getItems()) {
					if (food.getId() == f.getId()) {
						foodComboBox.getItems().remove(food);
						break;
					}
				}
				
				break;
			}
		}
	}

	@FXML
	void cancelChanges(ActionEvent event) {
		foodComboBox.getScene().getWindow().hide();
	}

	@FXML
	void deleteFood(ActionEvent event) {
		foodComboBox.getItems().add(foodDao.getById(selectedOverview.getFoodId()));
		orderModel.removePortionsOfFood(selectedOverview.getFoodId());
		portionsTableView.getItems().remove(selectedOverview);
		selectedOverview = null;
		deleteButton.setDisable(true);
	}

	@FXML
	void saveChanges(ActionEvent event) {
		Order savedOrder = orderModel.getOrderFromModel();
		orderDao.save(savedOrder);
		foodComboBox.getScene().getWindow().hide();
	}

}
