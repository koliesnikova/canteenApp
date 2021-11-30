package sk.upjs.paz1c.main;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

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
	
    @FXML
    private Button saveButton;
	
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
		nameTextField.textProperty().bindBidirectional(ingredientModel.nameProperty());
		priceTextField.textProperty().bindBidirectional(ingredientModel.priceProperty(), new NumberStringConverter() {
			@Override
			public Number fromString(String value) {
				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException e) {
					return -1;
				}
			}
		});
		
		//https://stackoverflow.com/questions/51032498/get-change-amount-with-listener-and-doubleproperty
		ingredientModel.priceProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.doubleValue() < 0) {
					saveButton.setDisable(true);
					priceTextField.setStyle("-fx-background-color: lightcoral");
				} else {
					saveButton.setDisable(false);
					priceTextField.setStyle("-fx-background-color: white");					
				}
				
			}
		});
		
		standardAmountTextField.textProperty().bindBidirectional(ingredientModel.amountProperty());
		setItemsStandardAmountToComboBox();
		standardAmountComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				newValue = newValue == null ? "" : newValue;
				ingredientModel.setUnit(newValue);
			}
		});
		
		//https://o7planning.org/11185/javafx-spinner
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
		amountAvailableSpinner.setValueFactory(valueFactory);
		// https://stackoverflow.com/questions/35835939/spinnerinteger-bind-to-integerproperty
		amountAvailableSpinner.getValueFactory().valueProperty().bindBidirectional(ingredientModel.amountAvailableProperty().asObject());
		
		

	}

	@FXML
	void cancelChanges(ActionEvent event) {
		nameTextField.getScene().getWindow().hide();
	}

	@FXML
	void saveChanges(ActionEvent event) {
		Ingredient ingredient = ingredientModel.getIngredient();
		ingredientDao.save(ingredient);
		nameTextField.getScene().getWindow().hide();
	}
	
	private void setItemsStandardAmountToComboBox() {
		List<String> items = new ArrayList<>();
		items.add("kg");
		items.add("L");
		items.add("ks");
		standardAmountComboBox.setItems(FXCollections.observableArrayList(items));
		if (ingredientModel.getUnit() != null)
			standardAmountComboBox.getSelectionModel().select(items.indexOf(ingredientModel.getUnit()));
	}

}
