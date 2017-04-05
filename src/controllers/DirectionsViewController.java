package controllers;

import core.KioskMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.path.Node;

import java.io.IOException;

/**
 * Created by mattm on 3/29/2017.
 */
public class DirectionsViewController implements IControllerWithParams {
    private Node startNode;
    private Node endNode;

    @FXML
    private Button backBtn;

    @FXML
    private void clickBack(ActionEvent event) {
        KioskMain.setScene("views/FinalDestSelectionView.fxml", this.startNode);
    }

    @Override
    public void initData(Object... data) {
        this.startNode = (Node)data[0];
        this.endNode = (Node)data[1];

        System.out.println(KioskMain.getPath().findPath(this.startNode, this.endNode));
    }
}
