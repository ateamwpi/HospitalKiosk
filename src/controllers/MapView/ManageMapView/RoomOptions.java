package controllers.MapView.ManageMapView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import controllers.DirectoryView.ManageDirectoryView.ManageDirectoryPopupController;
import controllers.DirectoryView.ManageDirectoryView.ManageDirectoryViewController;
import controllers.MapView.Map.DraggableNode;
import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.dir.Location;

import javax.swing.*;

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
    @FXML
    private TableColumn<Location, String> nameCol;
    @FXML
    private TableColumn<Location, String> typeCol;

    private DraggableNode clicked;

    public RoomOptions(ManageMapSnackbarController parent) {
        super(parent);

        this.nodeChanged();
        this.editEntries.setOnAction(this::editPressed);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("locTypeFriendly"));
    }

    private void editPressed(ActionEvent event) {
        ManageDirectoryPopupController dir = new ManageDirectoryPopupController(
                this.parent.getManageMapController().getManageMapViewController().getRoot(), this.clicked.getNode());
        dir.showCentered();
//        ManageDirectoryViewController dir = new ManageDirectoryViewController();
//        dir.getSearchBox().setText(this.clicked.getNode().getRoomName());
//        KioskMain.getUI().setScene(dir);
    }

    @Override
    public void initData(Object... data) {
        this.parent = (ManageMapSnackbarController)data[0];
    }

    @Override
    public void nodeChanged() {
        if(clicked != null) {
            roomName.textProperty().unbindBidirectional(clicked.previewRoomNameProperty());
            this.currentEntries.getItems().clear();
        }

        this.clicked = parent.getManageMapController().getSelectedNode();

        if(clicked != null) {
            roomName.textProperty().bindBidirectional(clicked.previewRoomNameProperty());
            this.currentEntries.getItems().addAll(this.clicked.getNode().getLocations());
            this.editEntries.setDisable(false);
        }
        else {
            roomName.setText("");
            this.editEntries.setDisable(true);
        }
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
