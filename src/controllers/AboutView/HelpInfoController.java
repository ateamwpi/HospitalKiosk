package controllers.AboutView;

import com.jfoenix.controls.JFXPopup;
import controllers.AbstractController;
import controllers.PopupView.IPopup;
import javafx.scene.Parent;
import javafx.scene.layout.Region;


/**
 * Created by Jonathan on 4/24/2017.
 */
public class HelpInfoController extends AbstractController implements IPopup {

    private Parent parent;
    private JFXPopup instance;

    public HelpInfoController(Parent parent) {
        this.parent = parent;
        this.instance = new JFXPopup(getRegion());
        showCentered();
    }

    @Override
    public String getURL() {
        return "resources/views/AboutView/HelpInfo.fxml";
    }

    @Override
    public JFXPopup getInstance() {
        return instance;
    }

    @Override
    public Region getRegion() {
        return (Region) getRoot();
    }

    @Override
    public Parent getParent() {
        return parent;
    }
}
