package controllers.NavigationDrawer;

import com.sun.org.apache.xalan.internal.xsltc.dom.KeyIndex;
import controllers.AboutView.AboutViewController;
import controllers.AboutView.HelpInfoController;
import controllers.AbstractController;
import controllers.Icon;
import controllers.LoginView.LoginViewController;
import controllers.DirectoryView.ManageDirectoryView.ManageDirectoryViewController;
import controllers.MapView.ManageMapView.ManageMapViewController;
import controllers.DirectoryView.DirectoryView.DirectoryViewController;
import controllers.PopupView.TextboxAlertViewController;
import core.KioskMain;
import core.Utils;
import core.exception.RoomNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import models.path.Node;
import models.timeout.TimeoutManager;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by mattm on 4/24/2017
 */
public class MenuItem extends AbstractController {

    private static final Double ICON_SIZE = 24.0;

    public enum EnumMenuItem {
        About("About This Application", "info", EnumMenuItem::aboutPressed),
        Login("Login", "login", EnumMenuItem::loginPressed),
        Logout("Log Out", "logout", EnumMenuItem::logoutPressed),
        ManageMap("Manage Map", "map", EnumMenuItem::manageMapPressed),
        ManageDir("Manage Directory", "dir", EnumMenuItem::manageDirPressed),
        SelectAlgo("Select Path Algorithm", "settings", EnumMenuItem::selectAlgoPressed),
        SelectKiosk("Select Kiosk Location", "settings", EnumMenuItem::selectKioskPressed),
        UserDir("View Directory", "dir", EnumMenuItem::userDirPressed),
        SelectTimeout("Select Timeout Delay", "settings", EnumMenuItem::timeoutPressed),
        HelpInfo("Info & Visiting Hours", "help", EnumMenuItem::infoPressed);

        final String path;
        final String text;
        final Consumer<Parent> onClick;

        EnumMenuItem(String text, String path, Consumer<Parent> onClick) {
            this.text = text;
            this.path = path;
            // TODO uncomment below
//            this.path = "/resources/icons/" + path + ".png";
            this.onClick = onClick;
        }

        private static void aboutPressed(Parent mainRoot) {
            KioskMain.getUI().setScene(new AboutViewController());
        }

        private static void loginPressed(Parent mainRoot) {
            new LoginViewController(mainRoot);
        }

        private static void logoutPressed(Parent mainRoot) {
            KioskMain.getLogin().logout();
        }

        private static void manageMapPressed(Parent mainRoot) {
            KioskMain.getUI().setScene(new ManageMapViewController());
        }

        private static void manageDirPressed(Parent mainRoot) {
            KioskMain.getUI().setScene(new ManageDirectoryViewController());
        }

        private static void selectAlgoPressed(Parent mainRoot) {
            Utils.showDropdown(mainRoot, "Select Algorithm",
                    "Choose which pathfinding algorithm the application should use.",
                    KioskMain.getPath().getAlgorithms(),
                    KioskMain.getPath().getSelectedAlgorithm().getName(),
                    algorithm -> KioskMain.getPath().selectAlgorithm(algorithm));
        }

        private static void selectKioskPressed(Parent mainRoot) {
            Utils.showDropdown(mainRoot,
                    "Select Kiosk Location",
                    "Choose the location that the Kiosk is currently at.",
                    KioskMain.getPath().getRoomNames(),
                    KioskMain.getDir().getTheKiosk().getNode().getRoomName(),
                    roomName -> {
                        Node n = null;
                        try {
                            n = KioskMain.getPath().getRoom(roomName);
                        } catch (RoomNotFoundException e1) {
                            // TODO
                        }
                        KioskMain.getDir().getTheKiosk().setNode(n);
                    });
        }

        private static void userDirPressed(Parent mainRoot) {
            new DirectoryViewController(mainRoot,
                    location -> System.out.println("you clicked " + location),
                    false);
        }

        private static void timeoutPressed(Parent mainRoot) {
            new TextboxAlertViewController(mainRoot,
                "Select Timeout Delay",
                "Choose how long (in ms) the application should wait before signing out and returning to the splash screen.",
                KioskMain.getTimeout().getDelay()+"",
                time -> {
                    KioskMain.getTimeout().setDelay(Integer.parseInt(time));
                    KioskMain.getDB().setVar(TimeoutManager.DELAY_VAR, time);
                },
                time -> {
                    try {
                        Integer.parseInt(time);
                        return true;
                    } catch (NumberFormatException ex) {
                        return false;
                    }
                });
        }

        private static void infoPressed(Parent mainRoot) {
            new HelpInfoController(mainRoot);
        }
    }

    private EnumMenuItem item;
    private Parent mainRoot;

    @FXML
    private HBox root;
    @FXML
    private Label menuIcon; // TODO change to Icon
    @FXML
    private Label menuLabel;

    MenuItem(EnumMenuItem m, Parent mainRoot) {
        super(m, mainRoot);
    }

    @Override
    public void initData(Object... data) {
        this.item = (EnumMenuItem) data[0];
        this.mainRoot = (Parent) data[1];
    }

    @FXML
    private void initialize() {
        // bind event handlers
        root.setOnMouseClicked(this::onPressed);

        menuLabel.setText(item.text);
        menuIcon.getStyleClass().add(this.item.path);
        // TODO uncomment below
//        menuIcon.setPath(item.path);
//        menuIcon.setSize(ICON_SIZE);

//        menuLabel.styleProperty().bind(
//            Bindings.when(root.hoverProperty())
//                    .then("-fx-text-fill: royalblue;")
//                    .otherwise("-fx-text-fill: black;")
//        );
    }

    @FXML
    private void onPressed(MouseEvent e) {
        this.item.onClick.accept(mainRoot);
    }

    public String getURL() {
        return "resources/views/NavigatinDrawer/MenuItem.fxml";
    }

}
