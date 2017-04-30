package controllers.MapView.ManageMapView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import controllers.AbstractController;
import controllers.MapView.Map.DraggableNode;
import controllers.PopupView.IPopup;
import core.KioskMain;
import core.Utils;
import core.exception.NameInUseException;
import core.exception.NodeInUseException;
import core.exception.WrongFloorException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import models.path.Node;

import java.util.*;

/**
 * Created by mattm on 4/26/2017.
 *
 */
public class ElevatorStaircaseMenu extends AbstractController implements IPopup {

    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXButton doneButton;
    @FXML
    private JFXCheckBox floor1;
    @FXML
    private JFXCheckBox floor2;
    @FXML
    private JFXCheckBox floor3;
    @FXML
    private JFXCheckBox floor4;
    @FXML
    private JFXCheckBox floor5;
    @FXML
    private JFXCheckBox floor6;
    @FXML
    private JFXCheckBox floor7;
    @FXML
    private AnchorPane root;

    private ArrayList<JFXCheckBox> floors = new ArrayList<>(Arrays.asList(
            floor1, floor2, floor3, floor4, floor5, floor6, floor7
    ));
    private HashMap<Integer, Node> elevators = new HashMap<>();
    private boolean[] initial = new boolean[7];
    private Parent parent;
    private JFXPopup instance;
    private String elevatorName;
    private DraggableNode clicked;

    public ElevatorStaircaseMenu(Parent parent, DraggableNode clicked) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
        this.elevatorName = elevatorName(clicked.getNode());
        this.clicked = clicked;
        this.nameField.setText(elevatorName);
        for(Node n : KioskMain.getPath().getGraph().values()) {
            if(n.getNodeType().equals(clicked.getNode().getNodeType()) && elevatorName(n).equals(elevatorName)) {
                floors.get(n.getFloor()-1).selectedProperty().set(true);
                initial[n.getFloor()-1] = true;
                elevators.put(n.getFloor()-1, n);
            }
        }

        doneButton.setOnAction(this::doneClicked);
    }

    private String elevatorName(Node n) {
        return n.getRoomName().substring(n.getRoomName().indexOf("Floor ")+6);
    }

    private String toElevatorName(int floor) {
        return Utils.strForNum(floor) + " Floor " + nameField.getText();
    }

    private void doneClicked(ActionEvent event) {
        // Validate data
        if(KioskMain.getPath().hasRoomName(nameField.getText())) {
            // error
            return;
        }

        // Update all names of the elevators in this row
        if(!elevators.equals(nameField.getText())) {
            for(Node n : elevators.values()) {
                try {
                    n.setRoomName(toElevatorName(n.getFloor()));
                    n.save();
                } catch (NameInUseException e) {}
            }
            clicked.previewRoomNameProperty().setValue(toElevatorName(clicked.getNode().getFloor()));
            clicked.save();
        }

        for (JFXCheckBox box : floors) {
            int index = floors.indexOf(box);
            int floor = index+1;
            boolean before = initial[index];
            boolean after = box.selectedProperty().get();
            if(!before && after) {
                // add
                Node n = new Node(clicked.getPreviewX(), clicked.getPreviewY(), floor, clicked.getNode().isRestricted(), clicked.getNode().getNodeType(), toElevatorName(floor));
                elevators.put(index, n);
                KioskMain.getPath().addNode(n);
            }
            else if(!after && before) {
                // remove
                Node n = elevators.get(index);
                try {
                    KioskMain.getPath().removeNode(n);
                } catch (NodeInUseException e) {
                    e.printStackTrace();
                }
                elevators.remove(index);
            }
        }

        for (Node n : elevators.values()) {
            n.removeCrossFloorConnections();
        }

        ArrayList<Node> els = new ArrayList<>(elevators.values());

        els.sort(Comparator.comparingInt(Node::getFloor));

        for(int i = 1; i < els.size(); i++) {
            try {
                els.get(i-1).addConnection(els.get(i));
            } catch (WrongFloorException e) {
                e.printStackTrace();
            }
        }

        clicked.resetPreviewConnections();
        clicked.save();

        this.getInstance().hide();
    }

    @FXML
    private void initialize() {

    }

    @Override
    public String getURL() {
        return "resources/views/MapView/ManageMapView/ElevatorStaircaseMenu.fxml";
    }

    @Override
    public JFXPopup getInstance() {
        return this.instance;
    }

    @Override
    public Region getRegion() {
        return (Region) this.getRoot();
    }

    @Override
    public Parent getParent() {
        return this.parent;
    }
}
