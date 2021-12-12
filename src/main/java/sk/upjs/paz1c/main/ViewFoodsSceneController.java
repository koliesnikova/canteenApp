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
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.EntityUndeletableException;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;

public class ViewFoodsSceneController {
	//TODO adjust all scenes' titles

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
