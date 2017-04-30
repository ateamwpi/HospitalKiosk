package controllers.NavigationDrawer;

import com.sun.org.apache.xalan.internal.xsltc.dom.KeyIndex;
import controllers.AboutView.AboutViewController;
import controllers.AboutView.HelpInfoController;
import controllers.AbstractController;
import controllers.LoginView.LoginViewController;
import controllers.DirectoryView.ManageDirectoryView.ManageDirectoryViewController;
import controllers.MapView.ManageMapView.ManageMapViewController;
import controllers.DirectoryView.DirectoryView.DirectoryViewController;
import controllers.MapView.MapView.MapViewController;
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

/**
 * Created by mattm on 4/24/2017
 */
public class MenuItem extends AbstractController {

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
        HelpInfo("Info & Visiting Hours", "help", EnumMenuItem::infoPressed),
        GetDirections("Get Directions", "path", EnumMenuItem::directionsPressed);

        final String path;
        final String text;
        final BiConsumer<MouseEvent, Parent> onClick;

        EnumMenuItem(String text, String path, BiConsumer<MouseEvent, Parent> onClick) {
            this.text = text;
            this.path = path;
            this.onClick = onClick;
        }

        private static void aboutPressed(MouseEvent e, Parent mainRoot) {
            KioskMain.getUI().setScene(new AboutViewController());
        }

        private static void loginPressed(MouseEvent e, Parent mainRoot) {
            LoginViewController login = new LoginViewController(mainRoot);
            login.showCentered();
        }

        private static void logoutPressed(MouseEvent e, Parent mainRoot) {
            KioskMain.getLogin().logout();
            KioskMain.getUI().setScene(new MapViewController());
        }

        private static void manageMapPressed(MouseEvent e, Parent mainRoot) {
            KioskMain.getUI().setScene(new ManageMapViewController());
        }

        private static void manageDirPressed(MouseEvent e, Parent mainRoot) {
            KioskMain.getUI().setScene(new ManageDirectoryViewController());
        }

        private static void selectAlgoPressed(MouseEvent e, Parent mainRoot) {
            Utils.showDropdown(mainRoot, "Select Algorithm", "Choose which pathfinding algorithm the application should use.",
                    KioskMain.getPath().getAlgorithms(), KioskMain.getPath().getSelectedAlgorithm().getName(),
                    (s) -> {
                KioskMain.getPath().selectAlgorithm(s);
//                KioskMain.getUI().getNavDrawer().deselectButtons();
            });
        }

        private static void selectKioskPressed(MouseEvent e, Parent mainRoot) {
            Utils.showDropdown(mainRoot, "Select Kiosk Location", "Choose the location that the Kiosk is currently at.",
                    KioskMain.getPath().getRoomNames(), KioskMain.getDir().getTheKiosk().getNode().getRoomName(),
                    (s) -> {
                        Node n = null;
                        try {
                            n = KioskMain.getPath().getRoom(s);
                        } catch (RoomNotFoundException e1) {
                            // TODO
                            // literally not reachable since we do error checking elsewhere, bad design i guess?
                        }
                        KioskMain.getDir().getTheKiosk().setNode(n);
//                        KioskMain.getUI().getNavDrawer().deselectButtons();
                    });
        }

        private static void userDirPressed(MouseEvent e, Parent mainRoot) {
            DirectoryViewController dir = new DirectoryViewController(mainRoot, (location -> {
                System.out.println("you clicked " + location);
            }), false);
            dir.showCentered();
        }

        private static void timeoutPressed(MouseEvent e, Parent mainRoot) {
            TextboxAlertViewController text = new TextboxAlertViewController(mainRoot, "Select Timeout Delay",
                    "Choose how long (in ms) the application should wait before signing out and returning to the splash screen.",
                    KioskMain.getTimeout().getDelay()+"", (t) -> {
                KioskMain.getTimeout().setDelay(Integer.parseInt(t));
                KioskMain.getDB().setVar(TimeoutManager.DELAY_VAR, t);
//                KioskMain.getUI().getNavDrawer().deselectButtons();
            }, (t) -> {
                try {
                    Integer.parseInt(t);
                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                }
            });
            text.showCentered();
        }

        private static void infoPressed(MouseEvent e, Parent mainRoot) {
            HelpInfoController help = new HelpInfoController(mainRoot);
            help.showCentered();
        }

        private static void directionsPressed(MouseEvent e, Parent mainRoot) {
            KioskMain.getUI().setScene(new MapViewController());
        }
    }

    private EnumMenuItem item;
    private Parent mainRoot;

    @FXML
    private HBox root;
    @FXML
    private Label menuIcon;
    @FXML
    private Label menuLabel;

    public MenuItem(EnumMenuItem m, Parent mainRoot) {
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
        menuLabel.getStyleClass().setAll("button-body");
        menuIcon.getStyleClass().setAll(this.item.path);

//        menuLabel.styleProperty().bind(
//            Bindings.when(root.hoverProperty())
//                    .then("-fx-text-fill: royalblue;")
//                    .otherwise("-fx-text-fill: black;")
//        );
    }

    @FXML
    private void onPressed(MouseEvent e) {
//        this.menuLabel.getStyleClass().setAll("button-bold");
        this.item.onClick.accept(e, mainRoot);
    }

//    public void deselectButton() {
//        this.menuLabel.getStyleClass().setAll("button-body");
//    }

    public String getURL() {
        return "resources/views/NavigatinDrawer/MenuItem.fxml";
    }

}
