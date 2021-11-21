package sk.upjs.paz1c.main;

import java.io.IOException;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;

public class ViewIngredientsSceneController {

	@FXML
	private CheckBox availabilityCheckBox;

	@FXML
	private ListView<Ingredient> ingredientsListView;

	private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
	private Ingredient selectedIngredient = null;

	@FXML
	void initialize() {
		List<Ingredient> ingredients = ingredientDao.getAll();
		ingredientsListView.setItems(FXCollections.observableArrayList(ingredients));

		availabilityCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					List<Ingredient> ingredients = ingredientDao.getAllAvailable();
					ingredientsListView.setItems(FXCollections.observableArrayList(ingredients));
				} else {
					List<Ingredient> ingredients = ingredientDao.getAll();
					ingredientsListView.setItems(FXCollections.observableArrayList(ingredients));
				}
			}
		});

		ingredientsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ingredient>() {

			@Override
			public void changed(ObservableValue<? extends Ingredient> observable, Ingredient oldValue,
					Ingredient newValue) {
				selectedIngredient = newValue;
			}
		});

		ingredientsListView.setOnMouseClicked(event -> {
			try {
				if (event.getClickCount() == 2) {
					CreateIngredientSceneController controller = new CreateIngredientSceneController(
							selectedIngredient);
					FXMLLoader loader = new FXMLLoader(getClass().getResource("createIngredientScene.fxml"));
					loader.setController(controller);
					Parent parent = loader.load();
					Scene scene = new Scene(parent);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.setTitle("Save ingredient");
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.showAndWait();

					if (availabilityCheckBox.isSelected()) {
						List<Ingredient> ingr = ingredientDao.getAllAvailable();
						ingredientsListView.setItems(FXCollections.observableArrayList(ingredients));
					} else {
						List<Ingredient> ingr = ingredientDao.getAll();
						ingredientsListView.setItems(FXCollections.observableArrayList(ingredients));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	@FXML
	void closeWindow(ActionEvent event) {
		availabilityCheckBox.getScene().getWindow().hide();
	}

}
