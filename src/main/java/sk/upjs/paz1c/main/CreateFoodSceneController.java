package sk.upjs.paz1c.main;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;
import com.sun.javafx.scene.SceneUtils;

import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;
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
	private TextArea descriptionTextArea;

	@FXML
	private TextField weightTextField;

	@FXML
	private ImageView imageView;

	@FXML
	private ComboBox<Ingredient> ingredientsComboBox;
	
	@FXML 
	private Button loadImageButton;

	private FoodFxModel foodFxModel;

	public CreateFoodSceneController(Food food) {
		foodFxModel = new FoodFxModel(food);
	}

	public CreateFoodSceneController() {
		foodFxModel = new FoodFxModel();
	}

	@FXML
	void save(ActionEvent event) {

	}

	@FXML
	void cancelChanges(ActionEvent event) {

	}

	@FXML
	void initialize() {
		nameTextField.textProperty().bindBidirectional(foodFxModel.nameProperty());
		//TODO overenie ci sa uspesne premenil string na cislo
		priceTextField.textProperty().bindBidirectional(foodFxModel.priceProperty(), new NumberStringConverter() {
			@Override
			public Number fromString(String value) {
				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException e) {
					return -1;
				}
			}
		});
		weightTextField.textProperty().bindBidirectional(foodFxModel.weightProperty(), new NumberStringConverter() {
			@Override
			public Number fromString(String value) {
				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException e) {
					return -1;
				}
			}
		});
		imageTextField.textProperty().bindBidirectional(foodFxModel.imagePathProperty());
		imageTextField.setDisable(true);
		descriptionTextArea.textProperty().bindBidirectional(foodFxModel.descriptionProperty());
		
		if(imageTextField.getText()!=null) {
			FileInputStream input;
			try {
				input = new FileInputStream(imageTextField.getText());
				BufferedImage bufferedImage = ImageIO.read(input);
				Image image = SwingFXUtils.toFXImage(bufferedImage, null);
				imageView.setImage(image);
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Image can not be opened. Invalid path or damaged file.");
				alert.show();
				e.printStackTrace();
			}
			
		}

//		//https://stackoverflow.com/questions/51032498/get-change-amount-with-listener-and-doubleproperty
//		foodFxModel.priceProperty().addListener(new ChangeListener<Number>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				if (newValue.doubleValue() < 0) {
//					saveButton.setDisable(true);
//					priceTextField.setStyle("-fx-background-color: lightcoral");
//				} else {
//					saveButton.setDisable(false);
//					priceTextField.setStyle("-fx-background-color: white");					
//				}
//				
//			}
//		});
//		

//		standardAmountComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//
//			@Override
//			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				newValue = newValue == null ? "" : newValue;
//				ingredientModel.setUnit(newValue);
//			}
//		});
//		

	}
	@FXML
	void loadImage(){
		//https://java-buddy.blogspot.com/2013/01/use-javafx-filechooser-to-open-image.html
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		java.io.File file = fileChooser.showOpenDialog(null);
		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			imageTextField.setText(file.getPath());
			Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			imageView.setImage(image);
		} catch (IOException ex) {
			
			ex.printStackTrace(); 
		}
	}
	

}
