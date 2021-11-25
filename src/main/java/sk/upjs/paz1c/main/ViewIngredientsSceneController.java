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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.EntityUndeletableException;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;

public class ViewIngredientsSceneController {

	@FXML
	private CheckBox availabilityCheckBox;

	@FXML
	private ListView<Ingredient> ingredientsListView;
	
    @FXML
    private Button deleteButton;

	private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
	private Ingredient selectedIngredient = null;

	@FXML
	void initialize() {
		deleteButton.setDisable(true);
		updateListView();

		availabilityCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateListView();
			}
		});

		ingredientsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ingredient>() {

			@Override
			public void changed(ObservableValue<? extends Ingredient> observable, Ingredient oldValue,
					Ingredient newValue) {
				if (newValue == null) {
					deleteButton.setDisable(true);
				} else {
					deleteButton.setDisable(false);
				}
				selectedIngredient = newValue;
			}
		});

		ingredientsListView.setOnMouseClicked(event -> {

			if (event.getClickCount() == 2) {
				CreateIngredientSceneController controller = new CreateIngredientSceneController(selectedIngredient);
				openSaveIngredientWindow(controller);

			}

		});

	}

	@FXML
	void closeWindow(ActionEvent event) {
		availabilityCheckBox.getScene().getWindow().hide();
	}

	@FXML
	void createNewIngredient(ActionEvent event) {
		CreateIngredientSceneController controller = new CreateIngredientSceneController();
		openSaveIngredientWindow(controller);
	}

	@FXML
	void deleteSelected(ActionEvent event) {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText("Do you really want to delete " + selectedIngredient.getName());
			Optional<ButtonType> buttonType = alert.showAndWait();
			if (buttonType.get() == ButtonType.OK) {
				ingredientDao.delete(selectedIngredient.getId());
				updateListView();
			}
		} catch (EntityUndeletableException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Ingredient can not be deleted, is part of some food!");
			alert.show();
		}
	}

	private void openSaveIngredientWindow(CreateIngredientSceneController controller) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("createIngredientScene.fxml"));
			loader.setController(controller);
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Save ingredient");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			// TODO take saved ingredient and update listview
			updateListView();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateListView() {
		if (availabilityCheckBox.isSelected()) {
			List<Ingredient> ingr = ingredientDao.getAllAvailable();
			ingredientsListView.setItems(FXCollections.observableArrayList(ingr));
		} else {
			List<Ingredient> ingr = ingredientDao.getAll();
			ingredientsListView.setItems(FXCollections.observableArrayList(ingr));
		}
	}

}
