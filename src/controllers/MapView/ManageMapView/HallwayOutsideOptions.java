package controllers.MapView.ManageMapView;

import controllers.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.Parent;

/**
 * Created by Madeline on 4/26/2017.
 */
public class HallwayOutsideOptions extends AbstractNodeOptions {

    @FXML
    private Parent root;

    public HallwayOutsideOptions(ManageMapSnackbarController parent) {
        super(parent);
    }

    @Override
    public String getURL() {
        return "resources/views/MapView/ManageMapView/HallwayOutsideOptions.fxml";
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
}
