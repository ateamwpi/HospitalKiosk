package controllers.NavigationDrawer;

import controllers.AbstractController;
import controllers.MapView.MapView.MenuItem;
import controllers.MapView.MapView.MenuItem.EnumMenuItem;
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
        this.mainRoot = (Parent)data[0];
    }

    @FXML
    private void initialize() {
        KioskMain.getLogin().attachObserver(this);
        this.onAccountChanged();
    }

    public Label getDrawerClose() {
        return drawerClose;
    }

    public Pane getScrim() {
        return this.scrim;
    }

    @Override
    public String getURL() {
        return "resources/views/OptionsMenu.fxml";
    }

    private void setMenuItems() {
        this.menuItems.getChildren().clear();
        for (EnumMenuItem e : KioskMain.getLogin().getState().getAvailableOptions()) {
            this.menuItems.getChildren().add(new MenuItem(e, mainRoot).getRoot());
        }
    }

    @Override
    public void onAccountChanged() {
        this.accountText.setText(KioskMain.getLogin().getState().getWelcomeMessage());
        this.setMenuItems();
    }

    public VBox getMenuItems() {
        return menuItems;
    }
}
