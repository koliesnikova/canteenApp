package sk.upjs.paz1c.main;

import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.Order;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;

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

	public CreateOrderSceneController(Order selectedOrder) {
		
	}

	public CreateOrderSceneController() {
		
	}

    @FXML
    void initialize() {

    }
    
    @FXML
    void addFood(ActionEvent event) {

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
