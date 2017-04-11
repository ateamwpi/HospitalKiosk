
import core.KioskMain;
import javafx.application.Application;
import javafx.scene.Parent;
import org.loadui.testfx.GuiTest;

/**
 * Created by dylan on 4/11/17.
 */
public abstract class AbstractUITest extends GuiTest {

    @Override
    public Parent getRootNode() {
        return null;
    }

    @Override
    public void setupStage() throws Throwable {
        new Thread(() -> Application.launch(KioskMain.class)).start();
    }

}
