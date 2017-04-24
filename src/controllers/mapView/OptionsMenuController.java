package controllers.mapView;

import controllers.AbstractController;
import controllers.mapView.MenuItem.EnumMenuItem;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Created by dylan on 4/16/17.
 */
public class OptionsMenuController extends AbstractController {

    @FXML
    private Label drawerClose;
    @FXML
    private Pane scrim;
    @FXML
    private VBox menuItems;

    private Parent mainRoot;

    public OptionsMenuController(Parent mainRoot) {
        super(mainRoot);
    }

    @Override
    public void initData(Object... data) {
        this.mainRoot = (Parent)data[0];
    }

    @FXML
    private void initialize() {
        menuItems.getChildren().add(new MenuItem(EnumMenuItem.About, mainRoot).getRoot());
        menuItems.getChildren().add(new MenuItem(EnumMenuItem.Login, mainRoot).getRoot());;
    }

    public Label getDrawerClose() {
        return drawerClose;
    }

    public Pane getScrim() {
        return this.scrim;
    }

    @Override
    public String getURL() {
        return "views/OptionsMenu.fxml";
    }
}
