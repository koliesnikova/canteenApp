package sk.upjs.paz1c.main;

import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.Ingredient;

public class IngredientInFoodController {

	@FXML
	private TextField amountNeededTextField;

	@FXML
	private Label ingredientNameLabel;

	@FXML
	private Label standardAmountLabel;

	@FXML
	private Button saveButton;

	@FXML
	private Spinner<Integer> amountNeededSpinner;

	private String name;
	private String amount;
	private Integer needed;
	
	public IngredientInFoodController(Ingredient ingredient, Food food) {
		this.name = ingredient.getName();
		this.amount = ingredient.getAmount();
		Map<Ingredient, Integer> allIngrs = food.getIngredients();
		for (Ingredient ingr : allIngrs.keySet()) {
			if (ingr.getId().equals(ingredient.getId())) {
				this.needed = allIngrs.get(ingr);
			}
		}
	}

	@FXML
	void initialize() {
		ingredientNameLabel.setText(name);
		standardAmountLabel.setText(amount);
		if (needed != null) {
			amountNeededTextField.setText(needed.toString());
		} else {
			amountNeededTextField.setText("0");
		}

		// https://o7planning.org/11185/javafx-spinner
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE, 0);
		amountNeededSpinner.setValueFactory(valueFactory);
		
		// TODO bind spinner?
		// https://stackoverflow.com/questions/35835939/spinnerinteger-bind-to-integerproperty
		//amountNeededSpinner.getValueFactory().valueProperty().bindBidirectional(foodFxModel.amountAvailableProperty().asObject());

	}

	@FXML
	void saveChanges(ActionEvent event) {
		// TODO implement
		
	}
}
