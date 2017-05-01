package controllers.NavigationDrawer;

import controllers.AbstractController;
import controllers.NavigationDrawer.MenuItem.EnumMenuItem;
import core.KioskMain;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.login.ILoginObserver;

/**
 * Created by dylan on 4/16/17.
 */
public class NavigationDrawerController extends AbstractController implements ILoginObserver {

    @FXML
    private Label drawerClose;
    @FXML
    private Pane scrim;
    @FXML
    private Label accountText;
    @FXML
    private VBox menuItems;

    private Parent mainRoot;

    public NavigationDrawerController(Parent mainRoot) {
        super(mainRoot);
    }

    @Override
    public void initData(Object... data) {
        mainRoot = (Parent)data[0];
    }

    @FXML
    private void initialize() {
        KioskMain.getLogin().attachObserver(this);
        onAccountChanged();
    }

    public Label getDrawerClose() {
        return drawerClose;
    }

    public Pane getScrim() {
        return scrim;
    }

    @Override
    public String getURL() {
        return "resources/views/NavigatinDrawer/NavigationDrawer.fxml";
    }

    private void setMenuItems() {
        menuItems.getChildren().clear();
        for (EnumMenuItem e : KioskMain.getLogin().getState().getAvailableOptions()) {
            menuItems.getChildren().add(new MenuItem(e, mainRoot).getRoot());
        }
    }

    @Override
    public void onAccountChanged() {
        accountText.setText(KioskMain.getLogin().getState().getWelcomeMessage());
        setMenuItems();
    }

    public VBox getMenuItems() {
        return menuItems;
    }
}
