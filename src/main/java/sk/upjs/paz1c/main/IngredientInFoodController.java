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
	private TextField amountNeededTextField; //delete

	@FXML
	private Label ingredientNameLabel;

	@FXML
	private Label standardAmountLabel;

	//@FXML
	//private Button saveButton;

	@FXML
	private Spinner<Integer> amountNeededSpinner;

	private String name;
	private String amount;
	private Integer needed = 0;
	private FoodFxModel foodModel;
	private Ingredient ingredient;

	public IngredientInFoodController(Ingredient ingredient, Food food, FoodFxModel foodModel) {
		this.ingredient = ingredient;
		this.name = ingredient.getName();
		this.amount = ingredient.getAmount();
		Map<Ingredient, Integer> allIngrs = food.getIngredients();
		for (Ingredient ingr : allIngrs.keySet()) {
			if (ingr.getId().equals(ingredient.getId())) {
				this.needed = allIngrs.get(ingr);
			}
		}
		this.foodModel = foodModel;
	}

	@FXML
	void initialize() {
		ingredientNameLabel.setText(name);
		standardAmountLabel.setText(amount);
		// https://o7planning.org/11185/javafx-spinner
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
				Integer.MAX_VALUE, 0);
		amountNeededSpinner.setValueFactory(valueFactory);

		if (needed != null) {
			amountNeededSpinner.getValueFactory().setValue(needed);
		} else {
			amountNeededSpinner.getValueFactory().setValue(0);
		}

		// https://stackoverflow.com/questions/35835939/spinnerinteger-bind-to-integerproperty
		 amountNeededSpinner.getValueFactory().valueProperty().bindBidirectional(foodModel.amountNeededProperty(ingredient, needed).asObject());
	}

	@FXML
	void saveChanges(ActionEvent event) {
		// TODO implement save amount needed
		foodModel.setAmountNeeded(ingredient.getId(), needed);
		ingredientNameLabel.getScene().getWindow().hide();

	}
}
