package controllers.MapView.ManageMapView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * Created by Madeline on 4/26/2017.
 */
public class RoomOptions extends AbstractNodeOptions {

    @FXML
    private JFXTextField roomName;
    @FXML
    private JFXButton editEntries;
    @FXML
    private TableView currentEntries;

    public RoomOptions(ManageMapSnackbarController parent) {
        super(parent);
    }

    @Override
    public void initData(Object... data) {
        this.parent = (ManageMapSnackbarController)data[0];
    }

    @Override
    public void nodeChanged() {

    }

    @Override
    public void savePressed() {

    }

    @Override
    public void cancelPressed() {

    }

    @Override
    public boolean hasUnsavedChanges() {
        return false;
    }

    @Override
    public String getURL() {
        return "resources/views/MapView/ManageMapView/RoomOptions.fxml";
    }
}
