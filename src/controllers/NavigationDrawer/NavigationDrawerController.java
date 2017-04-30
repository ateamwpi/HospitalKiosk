package controllers.NavigationDrawer;

import controllers.AbstractController;
import controllers.NavigationDrawer.MenuItem.EnumMenuItem;
import core.KioskMain;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.login.ILoginObserver;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dylan on 4/16/17.
 *
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
    private ArrayList<MenuItem> items;

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
        KioskMain.getUI().setNavDrawer(this);
        KioskMain.getLogin().attachObserver(this);
        items = new ArrayList<>();
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
        items.clear();
        for (EnumMenuItem e : KioskMain.getLogin().getState().getAvailableOptions()) {
            MenuItem m = new MenuItem(e, mainRoot);
            menuItems.getChildren().add(m.getRoot());
            items.add(m);
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

//    public void deselectButtons() {
//        for(MenuItem m : items) {
//            m.deselectButton();
//        }
//    }
}
