package controllers.MapView.ManageMapView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * Created by Madeline on 4/26/2017.
 */
public class RoomOptionsController extends AbstractController{

    @FXML
    private JFXTextField roomName;
    @FXML
    private JFXButton editEntries;
    @FXML
    private TableView currentEntries;


    @Override
    public String getURL() {
        return "resources/views/ManageMapView/RoomOptions";
    }
}
