package controllers.MapView.ManageMapView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import controllers.MapView.Map.DraggableNode;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

/**
 * Created by Madeline on 4/26/2017.
 *
 */
public class RoomOptions extends AbstractNodeOptions {

    @FXML
    private JFXTextField roomName;
    @FXML
    private JFXButton editEntries;
    @FXML
    private TableView currentEntries;

    private DraggableNode clicked;

    public RoomOptions(ManageMapSnackbarController parent) {
        super(parent);

        this.nodeChanged();
    }

    @Override
    public void initData(Object... data) {
        this.parent = (ManageMapSnackbarController)data[0];
    }

    @Override
    public void nodeChanged() {
        if(clicked != null)
            roomName.textProperty().unbindBidirectional(clicked.previewRoomNameProperty());

        this.clicked = parent.getManageMapController().getSelectedNode();

        if(clicked != null)
            roomName.textProperty().bindBidirectional(clicked.previewRoomNameProperty());
        else
            roomName.setText("");
    }

    @Override
    public void savePressed() {
//        this.clicked.previewRoomNameProperty().setValue(roomName.getText());
    }

    @Override
    public void cancelPressed() {
//        roomName.setText(this.clicked.getPreviewRoomName());
    }

    @Override
    public boolean hasUnsavedChanges() {
        return false;
    }

    @Override
    public String getRoomName() {
        return roomName.getText();
    }

    @Override
    public String getURL() {
        return "resources/views/MapView/ManageMapView/RoomOptions.fxml";
    }
}
