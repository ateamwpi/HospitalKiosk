package models.qr;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import controllers.AbstractController;
import controllers.PopupView.IPopup;
import core.KioskMain;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zack on 4/29/2017.
 */
public class QRManager extends AbstractController implements IPopup {

    @FXML
    private JFXButton okButton;
    @FXML
    private javafx.scene.control.Label alertTitle;
    @FXML
    private Text alertBody;
    @FXML
    private AnchorPane root;
    private Parent parent;
    private JFXPopup instance;
    private Consumer<Event> onClose;

    public QRManager (Parent parent, String title, String body) {
        this(parent, title, body, null);
    }

    public QRManager(Parent parent, String title, String body, Consumer<javafx.event.Event> onClose) {
        this.parent = parent;
        this.instance = new JFXPopup(this.getRegion());
        this.alertTitle.setText(title);
        this.alertBody.setText(body);
        this.onClose = onClose;

        this.root.setOnKeyPressed(event -> {
            System.out.println("ALERT PRESSED");
            if (event.getCode().equals(KeyCode.ENTER)) {
                if(this.onClose != null) onClose.accept(event);
                this.getInstance().hide();
                event.consume();
            }
        });

        showCentered();
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

    @Override
    public String getURL() {
        return "resources/views/PopupView/Alert.fxml";
    }

    public void makeQRCode (Stage stage, String directions) {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 300;
        int height = 300;
        String fileType = "png";

        BufferedImage bufferedImage = null;
        try {
            BitMatrix byteMatrix = qrCodeWriter.encode(directions, BarcodeFormat.QR_CODE, width, height);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();

            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            System.out.println("Success...");

        } catch (WriterException ex) {
            Logger.getLogger(QRManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        ImageView qrView = new ImageView();
        qrView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));

        StackPane root = new StackPane();
        root.getChildren().add(qrView);

        Scene scene = new Scene(root, 350, 350);

        stage.setScene(scene);
        stage.show();
    }


}
