package sk.upjs.paz1c.main;

import sk.upjs.paz1c.storage.Ingredient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class CreateIngredientSceneController {
	@FXML
	private Spinner<?> amountAvailableSpinner;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField priceTextField;

	@FXML
	private ComboBox<?> standardAmountComboBox;

	@FXML
	private TextField standardAmountTextField;

	
	public CreateIngredientSceneController(Ingredient ingredient) {
		// TODO Auto-generated constructor stub
	}

	public CreateIngredientSceneController() {
		// TODO Auto-generated constructor stub
	}
	

	@FXML
	void initialize() {

	}

	@FXML
	void cancelChanges(ActionEvent event) {

	}

	@FXML
	void saveChanges(ActionEvent event) {

	}

}
