package sk.upjs.paz1c.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.EntityUndeletableException;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;

public class ViewFoodsSceneController {

	@FXML
	private CheckBox inOrdersCheckBox;

	@FXML
	private Button deleteButton;

	@FXML
	private ListView<Food> foodListView;

	@FXML
	private Button createFoodButton;

	private Food selectedFood = null;
	private FoodDao foodDao = DaoFactory.INSTANCE.getFoodDao();

	@FXML
	void initialize() {
		deleteButton.setDisable(true);
		updateListView("all");

		foodListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Food>() {
			@Override
			public void changed(ObservableValue<? extends Food> observable, Food oldValue, Food newValue) {
				if (newValue == null) {
					deleteButton.setDisable(true);
				} else {
					deleteButton.setDisable(false);
				}
				selectedFood = newValue;
			}
		});

		foodListView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				CreateFoodSceneController controller = new CreateFoodSceneController(selectedFood);
				openSaveFoodWindow(controller);
				if (inOrdersCheckBox.isSelected()) {
					updateListView("inOrders");
				} else {
					updateListView("all");
				}
			}
		});

		inOrdersCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					updateListView("inOrders");
				} else {
					updateListView("all");
				}
			}
		});
	}

	@FXML
	void deleteFood(ActionEvent event) {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText("Do you really want to delete " + selectedFood.getName() + " ?");
			Optional<ButtonType> buttonType = alert.showAndWait();
			if (buttonType.get() == ButtonType.OK) {
				foodDao.delete(selectedFood.getId());
				if (inOrdersCheckBox.isSelected()) {
					updateListView("available");
				} else {
					updateListView("all");
				}
			}
		} catch (EntityUndeletableException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Food can not be deleted. It is in some order.");
			alert.show();
		}
	}

	@FXML
	void openCreateFood(ActionEvent event) {
		try {
			CreateFoodSceneController controller = new CreateFoodSceneController();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("createFoodScene.fxml"));
			loader.setController(controller);

			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Create food");
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO take saved food and update listview
	}

	private void openSaveFoodWindow(CreateFoodSceneController controlller) { //TODO toto pomocou modelu
		try {
			CreateFoodSceneController controller = new CreateFoodSceneController();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("createFoodScene.fxml"));
			loader.setController(controller);

			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Edit food");

			Text title = controller.getTitleLabel();
			title.setText("Edit food");


			TextField field = controller.getNameTextField();
			field.setText(selectedFood.getName());

			field = controller.getPriceTextField();
			field.setText(selectedFood.getPrice().toString());

			field = controller.getWeightTextField();
			field.setText(selectedFood.getWeight().toString());

			TextArea area = controller.getDescriptionTextArea();
			area.setText(selectedFood.getDescription());

			field = controller.getImageTextField();
			field.setText(selectedFood.getImage_url());

			//Ingredients selection
			VBox ingredients = controller.getIngredientVbox();
			VBox amount = controller.getAmountVbox();
			VBox needed = controller.getAmountNeededVbox();
			
			ObservableList<Node> vingredients = ingredients.getChildren();
			ObservableList<Node> vamount = amount.getChildren();
			ObservableList<Node> vneeded = needed.getChildren();

			IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
			List<Ingredient> allIngrs = ingredientDao.getAll();

			Map<Long, Integer> map = selectedFood.getIngredientsById();
			
			for (Ingredient ingredient : allIngrs) {
				vingredients.add(new Label (ingredient.getName()));
				vamount.add(new Label (ingredient.getAmount()));

				field = new TextField();
				field.setEditable(true);
				if (map.get(ingredient.getId())!=null) {
					//get amount needed
					field.setText(map.get(ingredient.getId()).toString());
				}else {
					field.setText("0");
				}
				vneeded.add(field);
			}
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO take edited food and update listview
	}

	private void updateListView(String method) {
		if (method.equals("all")) {
			List<Food> food = foodDao.getAll();
			foodListView.setItems(FXCollections.observableArrayList(food));
		} else {
			List<Food> foods = foodDao.getFoodsInOrders();
			foodListView.setItems(FXCollections.observableArrayList(foods));
		}
	}

}
