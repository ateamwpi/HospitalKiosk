package controllers.MapView.ManageMapView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableView;

/**
 * Created by Madeline on 4/26/2017.
 */
public class HallwayOutsideOptions extends AbstractController{

    @FXML
    private Parent root;


    @Override
    public String getURL() {
        return "resources/views/ManageMapView/HallwayOutsideOptions.fxml";
    }
}
