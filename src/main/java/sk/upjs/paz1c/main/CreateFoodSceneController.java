package sk.upjs.paz1c.main;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;

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
	private ListView<String> ingredientListView;

	@FXML
	private Button loadImageButton;

	private FoodFxModel foodFxModel;
	private FoodDao foodDao = DaoFactory.INSTANCE.getFoodDao();
	private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
	private List<String> ingredientInFood = new ArrayList<String>();
	private String selectedIngredient = null;
	private Food actualFood = null;

	public CreateFoodSceneController(Food food) {
		foodFxModel = new FoodFxModel(food);
		actualFood = food;
	}

	public CreateFoodSceneController() {
		foodFxModel = new FoodFxModel();
	}

	@FXML
	void save(ActionEvent event) {
		if(foodFxModel.getName()!=null && (!foodFxModel.getName().isBlank())) {
		actualFood = foodDao.save(foodFxModel.getFood());
		nameTextField.getScene().getWindow().hide();
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("You can't save food without naming it!");
			alert.show();
		}
	}

	@FXML
	void cancelChanges(ActionEvent event) {
		cancelButton.getScene().getWindow().hide();
	}

	@FXML
	void initialize() {
		nameTextField.textProperty().bindBidirectional(foodFxModel.nameProperty());
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

		// https://stackoverflow.com/questions/51032498/get-change-amount-with-listener-and-doubleproperty
		foodFxModel.priceProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.doubleValue() < 0) {
					submitButton.setDisable(true);
					priceTextField.setStyle("-fx-background-color: lightcoral");
				} else {
					submitButton.setDisable(false);
					priceTextField.setStyle("-fx-background-color: white");
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
		foodFxModel.weightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.doubleValue() < 0) {
					submitButton.setDisable(true);
					weightTextField.setStyle("-fx-background-color: lightcoral");
				} else {
					submitButton.setDisable(false);
					weightTextField.setStyle("-fx-background-color: white");
				}
			}
		});
		
		setIngredientInFood();
		
		//https://stackoverflow.com/questions/35249058/set-items-colors-in-listview-in-javafx
		//https://www.tabnine.com/code/java/methods/javafx.scene.control.ListView/setCellFactory
		ingredientListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> list) {
				return new ListCell<String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
					        setText(null);
					        setGraphic(null);
					    } else {
					    	setText(item);
							setIngredientInFood();
							setTextFill(ingredientInFood.contains(item) ? Color.BLUE : Color.BLACK);
					    }
					}
				};
			}
		});

		ingredientListView.setItems(FXCollections.observableArrayList(ingredientDao.getAllNames()));

		ingredientListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				selectedIngredient = newValue;
				
			}
		}); 
		
		imageTextField.textProperty().bindBidirectional(foodFxModel.imagePathProperty());
		descriptionTextArea.textProperty().bindBidirectional(foodFxModel.descriptionProperty());

		if (imageTextField.getText() != null) {
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
		
		ingredientListView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				List<Ingredient> ingrs = ingredientDao.getAll();
				Ingredient selectedI = null;
				for (Ingredient ingredient : ingrs) {
					if(selectedIngredient.equals(ingredient.getName())) {
						selectedI = ingredient;
						break;
					}
				}
				
				if(actualFood==null) {
					actualFood=foodFxModel.getFood();
				}
				
				IngredientInFoodController controller = new IngredientInFoodController(selectedI,actualFood, foodFxModel);
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("ingredientInFoodScene.fxml"));
					loader.setController(controller);

					Parent parent = loader.load();
					Scene scene = new Scene(parent);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.setResizable(false);
					stage.setTitle("Amount of ingredient in food");
					stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
				setIngredientInFood();
				ingredientListView.refresh();
				actualFood = foodFxModel.getFood();				
			}
		});
		

	}

	@FXML
	void loadImage() {
		// https://java-buddy.blogspot.com/2013/01/use-javafx-filechooser-to-open-image.html
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
	
	void setIngredientInFood() {
		ingredientInFood.clear();
		List<Ingredient> inFood = foodFxModel.getIngredientsInFood();
		for (Ingredient ingredient : inFood) {
			ingredientInFood.add(ingredient.getName());
		}
	}
	

}
