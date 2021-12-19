package sk.upjs.paz1c.main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sk.upjs.paz1c.biznis.DefaultCanteenManager;
import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.OrderDao;

public class MainSceneController {
	   @FXML
	    private Tooltip ingredientToolTip;

	    @FXML
	    private Tooltip shopToolTip;

	    @FXML
	    private Tooltip order2ToolTip;

	    @FXML
	    private Tooltip food2ToolTip;

	    @FXML
	    private Tooltip ingredient2ToolTip;

	    @FXML
	    private Tooltip orderToolTip;

	    @FXML
	    private Tooltip foodToolTip;

	    @FXML
	    private Button viewFoodsButton;

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
	    	//https://stackoverflow.com/questions/26854301/how-to-control-the-javafx-tooltips-delay
	    	foodToolTip.setShowDelay(Duration.millis(2));
	    	food2ToolTip.setShowDelay(Duration.millis(2));
	    	ingredientToolTip.setShowDelay(Duration.millis(2));
	    	ingredient2ToolTip.setShowDelay(Duration.millis(2));
	    	orderToolTip.setShowDelay(Duration.millis(2));
	    	order2ToolTip.setShowDelay(Duration.millis(2));
	    	shopToolTip.setShowDelay(Duration.millis(2));
	    	
	    	updateCounts();
	    	
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
					updateCounts();
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
					updateCounts();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	});
	    	
	    }
	    
	    private Stage openWindow(String title, FXMLLoader loader) {
			try {
				Parent parent = loader.load();
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				if (title.equals("Orders"))
					stage.setOnCloseRequest(e -> updateCounts());
				stage.setScene(scene);
				stage.setTitle(title);
				stage.show();
				
				return stage;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
	    }

	    
	    private void updateCounts() {
	    	viewOrdersButton.setText("( " + orderDao.getByPrepared(false).size() + " )");
	    	shoppingListButton.setText("( " + new DefaultCanteenManager().getNumberOfToBuy() + " )");
	    }
	    
	    
	    
}
