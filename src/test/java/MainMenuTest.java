
import javafx.scene.Node;
import org.junit.Test;
import org.loadui.testfx.exceptions.NoNodesFoundException;

/**
 * Created by dylan on 4/11/17.
 */
public class MainMenuTest extends AbstractUITest {

    @Test(expected = NoNodesFoundException.class)
    public void testButtons() {
        click("#viewMapBtn");
        Node btn = find("#viewMapBtn");
    }

}


