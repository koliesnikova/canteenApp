package sk.upjs.paz1c.main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.OrderDao;

public class MainSceneController {

	    @FXML
	    private Button viewFoodsButton;

	    @FXML
	    private Label numOfToBuyLabel;

	    @FXML
	    private Label numOfOrdersLabel;

	    @FXML
	    private Button createOrderButton;

	    @FXML
	    private Button createIngredientButton;

	    @FXML
	    private Button viewOrdersButton;

	    @FXML
	    private Button viewIngredientsButton;

	    @FXML
	    private Button createFoodButton;

	    @FXML
	    private Button shoppingListButton;
	    
	    private OrderDao orderDao = DaoFactory.INSTANCE.getOrderDao();

	    @FXML
	    void initialize() {
	    	viewFoodsButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					ViewFoodsSceneController controller = new ViewFoodsSceneController();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("viewFoodsScene.fxml"));				
					loader.setController(controller);
					openWindow("Foods", loader);	
				}
			});
	    	viewIngredientsButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					ViewIngredientsSceneController controller = new ViewIngredientsSceneController();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("viewIngredientsScene.fxml"));				
					loader.setController(controller);
					openWindow("Ingredients", loader);	
					
				}
			});
	    	viewOrdersButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					ViewOrdersSceneController controller = new ViewOrdersSceneController();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("viewOrdersScene.fxml"));				
					loader.setController(controller);
					openWindow("Orders", loader);	
					
				}
			});
	    	createIngredientButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						CreateIngredientSceneController c = new CreateIngredientSceneController();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("createIngredientScene.fxml"));				
						loader.setController(c);
						Parent parent = loader.load();
						
						Stage stage = new Stage();
						Scene scene = new Scene(parent);
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.setScene(scene);
						stage.setTitle("Create ingredient");
						stage.showAndWait();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			});
	    	
	    	createFoodButton.setOnAction(event -> {
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
	    	});
	    	
	    	
	    	createOrderButton.setOnAction(event -> {
	    		try {
					CreateOrderSceneController controller = new CreateOrderSceneController();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("createOrderScene.fxml"));
					loader.setController(controller);
					
					Parent parent = loader.load();
					Scene scene = new Scene(parent);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.setTitle("Create order");
					stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	});
	    	shoppingListButton.setOnAction(event -> {
	    		try {
					ShoppingListSceneController controller = new ShoppingListSceneController();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("shoppingListScene.fxml"));
					loader.setController(controller);
					Parent parent = loader.load();
					Scene scene = new Scene(parent);
					Stage stage = new Stage();
					stage.setScene(scene);
					stage.setTitle("Shopping list");
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	});
	    	
	    	//TODO vyhrat sa s Lables - pocet objednavok a veci na nakup - maybe - one day 
	    	updateLabels();
	    }
	    
	    private Stage openWindow(String title, FXMLLoader loader) {
			try {
				Parent parent = loader.load();
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle(title);
				stage.show();
				return stage;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
	    }

	    private void updateLabels() {
	    	int notPreparedOrders = orderDao.getByPrepared(false).size();
	    	String text = "You have " + notPreparedOrders;
	    	text += notPreparedOrders == 1 ? " order to prepare." : " orders to prepare.";
	    	numOfOrdersLabel.setText(text);
	    }
	    
	    
	    
}
