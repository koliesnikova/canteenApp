package sk.upjs.paz1c.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import sk.upjs.paz1c.storage.Ingredient;

public class ShoppingListSceneController {

	@FXML
	private TableView<Ingredient> toBuyTable;

	@FXML
	private DatePicker datePicker;

	@FXML
	private TableColumn<Ingredient, String> ingredientCol;

	@FXML
	private TableColumn<Ingredient, String> amountCol;

	@FXML
	    void initialize(){
		
		//TODO - which method will work? first biznis logic needed for calculations
	    	ingredientCol.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<Ingredient,String>, ObservableValue<String>>() {

				@Override
				public ObservableValue<String> call(CellDataFeatures<Ingredient, String> param) {
					SimpleStringProperty name = new SimpleStringProperty(param.getValue().getName()); 
					return name;
				}
			});
	    			
	    //toBuyTable.getColumns().add(ingredientCol);
	    
	    amountCol.setCellValueFactory(cellData -> {
	        String amountToBuy = cellData.getValue().getAmount();//getAmountToBuy
	        return new SimpleStringProperty(amountToBuy);
	    });
	    
	    }

}
