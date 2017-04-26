package models.timeout;

import controllers.WelcomeScreenController;
import core.KioskMain;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zack on 4/24/2017.
 */
public class TimeoutManager {

    private Timer timer;

    public TimeoutManager(){}

    public synchronized void resetTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }

        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(
                        () -> {
                            KioskMain.getUI().setScene(new WelcomeScreenController());
                        });
            }
        };

        timer.schedule(task, 15000);
    }
}
