package sk.upjs.paz1c.main;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.EntityUndeletableException;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;

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
		updateListView();

		inOrdersCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateListView();
			}
		});
		foodListView.setFixedCellSize(30.0);
		//https://stackoverflow.com/questions/33592308/javafx-how-to-put-imageview-inside-listview
		foodListView.setCellFactory(param -> new ListCell<Food>() {
			private ImageView imageView = new ImageView();

			@Override
			public void updateItem(Food food, boolean empty) {
				super.updateItem(food, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					imageView.setPreserveRatio(true);
					imageView.setFitHeight(foodListView.getFixedCellSize());
					if (food.getImage_url() != null && food.getImage_url() != "") {
						String path = food.getImage_url().replaceAll("\\\\", "/");
						imageView.setImage(new Image(path));
						
					}else {
						imageView.setImage(new Image("icons/fast-food.png"));
					}
					setGraphic(imageView);
					setText(food.toString());
				}
			}
		});

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
				updateListView();
			}
		} catch (EntityUndeletableException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Food can not be deleted. It is in some order.");
			alert.show();
		}
	}

	@FXML
	void openCreateFood(ActionEvent event) {
		CreateFoodSceneController controller = new CreateFoodSceneController();
		try {

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
		updateListView();
	}

	private void openSaveFoodWindow(CreateFoodSceneController controller) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("createFoodScene.fxml"));
			loader.setController(controller);

			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Edit food");

			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateListView();
	}

	private void updateListView() {
		if (inOrdersCheckBox.isSelected()) {
			List<Food> foods = foodDao.getFoodsInOrders();
			foodListView.setItems(FXCollections.observableArrayList(foods));
		} else {
			List<Food> food = foodDao.getAll();
			foodListView.setItems(FXCollections.observableArrayList(food));
		}
	}

}
