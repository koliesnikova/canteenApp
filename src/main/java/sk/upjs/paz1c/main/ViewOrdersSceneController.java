package sk.upjs.paz1c.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

public class ViewOrdersSceneController {

    @FXML
    private DatePicker dayDatePicker;

    @FXML
    private ListView<?> orderListView;

    @FXML
    void initialize() {

    }
    
    @FXML
    void closeWindow(ActionEvent event) {
    	
    }

}

