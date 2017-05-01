package controllers.MapView.ManageMapView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import controllers.MapView.Map.DraggableNode;
import controllers.MapView.Map.ManageMapController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import models.path.NodeType;

import java.util.ArrayList;

/**
 * Created by Madeline on 4/26/2017.
 *
 */
public class ManageMapDrawerController extends AbstractController{

    private ManageMapController manageMapController;
    private DraggableNode selectedNode;
    private StringProperty xTextProperty;
    private StringProperty yTextProperty;
    private BooleanProperty restrictedProperty;
    private StringConverter<Number> converter;

    private ArrayList<String> floorList;

    @FXML
    protected JFXTextField x;
    @FXML
    protected JFXTextField y;
    @FXML
    JFXButton saveNode;
    @FXML
    protected JFXButton cancel; //will discard changes
    @FXML
    JFXComboBox nodeType;
    @FXML
    JFXCheckBox employeeOnly;
    @FXML
    private Parent mainRoot;
    @FXML
    JFXButton nodeAction;
    @FXML
    private Label hamburger;
    @FXML
    private Pane root;
    @FXML
    private HBox bottomBox;
    @FXML
    private VBox contentBox;
    @FXML
    private Label drawerClose;

    private AbstractNodeOptions content;


    ManageMapDrawerController(Parent mainRoot, ManageMapController manageMapController) {
        super(mainRoot, manageMapController);
    }

    @Override
    public void initData(Object... data) {
        this.mainRoot = (Parent) data[0];
        this.manageMapController = (ManageMapController) data[1];
    }

    @Override
    public String getURL() {
        return "resources/views/MapView/ManageMapView/ManageMapDrawer.fxml";
    }

    @FXML
    public void initialize() {
        nodeAction.setText("Delete");
        saveNode.setDisable(true);
        cancel.setDisable(true);
        bottomBox.prefHeightProperty().bind(root.heightProperty().subtract(141));
        updateContent(NodeType.Location);
    }

    Label getHamburgerButton() {
        return hamburger;
    }

    void updateContent(NodeType nodeType) {
        contentBox.getChildren().clear();
        content = nodeType.makeController(this);
        contentBox.getChildren().add(content.getRoot());
    }

    void nodeChanged() {
        content.nodeChanged();
    }

    void savePressed() {
        content.savePressed();
    }

    void cancelPressed() {
        content.cancelPressed();
    }

    public boolean hasUnsavedChanges() {
        return content.hasUnsavedChanges();
    }

    public String getRoomName() {
        return content.getRoomName();
    }

    public Label getDrawerClose() {
        return drawerClose;
    }

    ManageMapController getManageMapController() {
        return manageMapController;
    }
}
