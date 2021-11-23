package sk.upjs.paz1c.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.Ingredient;

public class CreateFoodSceneController {

	@FXML
	private TextField imageTextField;

	@FXML
	private Button submitButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Text titleLabel;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField priceTextField;

	@FXML
	private VBox ingredientVbox;

	@FXML
	private VBox amountVbox;

	@FXML
	private VBox amountNeededVbox;

	@FXML
	private TextArea descriptionTextArea;

	@FXML
	private TextField weightTextField;

	public CreateFoodSceneController(Food food) {
		// TODO Auto-generated constructor stub
	}

	public CreateFoodSceneController() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	void save(ActionEvent event) {

	}

	@FXML
	void cancelChanges(ActionEvent event) {

	}

	public TextField getImageTextField() {
		return imageTextField;
	}

	public void setImageTextField(TextField imageTextField) {
		this.imageTextField = imageTextField;
	}

	public Text getTitleLabel() {
		return titleLabel;
	}

	public void setTitleLabel(Text titleLabel) {
		this.titleLabel = titleLabel;
	}

	public TextField getNameTextField() {
		return nameTextField;
	}

	public void setNameTextField(TextField nameTextField) {
		this.nameTextField = nameTextField;
	}

	public TextField getPriceTextField() {
		return priceTextField;
	}

	public void setPriceTextField(TextField priceTextField) {
		this.priceTextField = priceTextField;
	}


	public VBox getIngredientVbox() {
		return ingredientVbox;
	}

	public void setIngredientVbox(VBox ingredientVbox) {
		this.ingredientVbox = ingredientVbox;
	}

	public VBox getAmountVbox() {
		return amountVbox;
	}

	public void setAmountVbox(VBox amountVbox) {
		this.amountVbox = amountVbox;
	}

	public VBox getAmountNeededVbox() {
		return amountNeededVbox;
	}

	public void setAmountNeededVbox(VBox amountNeededVbox) {
		this.amountNeededVbox = amountNeededVbox;
	}

	public TextArea getDescriptionTextArea() {
		return descriptionTextArea;
	}

	public void setDescriptionTextArea(TextArea descriptionTextField) {
		this.descriptionTextArea = descriptionTextField;
	}

	public TextField getWeightTextField() {
		return weightTextField;
	}

	public void setWeightTextField(TextField weightTextField) {
		this.weightTextField = weightTextField;
	}

}
