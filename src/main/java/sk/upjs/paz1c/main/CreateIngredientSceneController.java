package sk.upjs.paz1c.main;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class CreateIngredientSceneController {
	@FXML
	private Spinner<Integer> amountAvailableSpinner;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField priceTextField;

	@FXML
	private ComboBox<String> standardAmountComboBox;

	@FXML
	private TextField standardAmountTextField;
	
	private IngredientFxModel ingredientModel;
	private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();

	
	public CreateIngredientSceneController(Ingredient ingredient) {
		ingredientModel = new IngredientFxModel(ingredient);
	}

	public CreateIngredientSceneController() {
		ingredientModel = new IngredientFxModel();
	}
	

	@FXML
	void initialize() {

	}

	@FXML
	void cancelChanges(ActionEvent event) {
		nameTextField.getScene().getWindow().hide();
	}

	@FXML
	void saveChanges(ActionEvent event) {

	}

}
