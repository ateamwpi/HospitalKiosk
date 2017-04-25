package controllers;

import controllers.AbstractPopupController;
import javafx.fxml.FXML;
import javafx.scene.Parent;


/**
 * Created by Jonathan on 4/24/2017.
 */
public class HelpInfoController extends AbstractPopupController {

    public HelpInfoController(Parent parent){super(parent);}

    @Override
    public String getURL() {
        return "views/HelpInfo.fxml";
    }
}
