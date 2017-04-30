package controllers.MapView.ManageMapView;

import com.jfoenix.controls.*;
import controllers.AbstractController;
import controllers.MapView.Map.DraggableNode;
import controllers.MapView.Map.ManageMapController;
import controllers.MapView.Map.MapController;
import controllers.MapView.MapView.MapViewController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import controllers.NavigationDrawer.NavigationDrawerController;
import core.KioskMain;
import core.Utils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import models.path.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Created by Madeline on 4/26/2017.
 */
public class ManageMapSnackbarController extends AbstractController{

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
    protected Label room;
    @FXML
    protected JFXButton saveNode;
    @FXML
    protected JFXButton deleteNode;
    @FXML
    protected JFXButton cancel; //will discard changes
    @FXML
    protected JFXComboBox nodeType;
    @FXML
    protected JFXCheckBox employeeOnly;
    @FXML
    protected Parent mainRoot;
    @FXML
    protected JFXButton nodeAction;
    @FXML
    protected Label hamburger;
    @FXML
    private VBox contentBox;


    public ManageMapSnackbarController(Parent mainRoot, ManageMapController manageMapController) {
        super(mainRoot);
        this.manageMapController = manageMapController;
    }

    @Override
    public void initData(Object... data) {
        mainRoot = (Parent)data[0];
    }



    @Override
    public String getURL() {
        return "resources/views/MapVIew/ManageMapView/ManageMapSnackbar.fxml";
    }

    @FXML
    public void initialize() {
        nodeAction.setDisable(true);
    }


    public Label getHamburgerButton() {
        return hamburger;
    }




}
