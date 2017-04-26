package models.timeout;

import controllers.WelcomeView.WelcomeViewController;
import core.KioskMain;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zack on 4/24/2017.
 */
public class TimeoutManager {

    private Timer timer;
    private int delay;

    public TimeoutManager() {
        this.delay = 90000;
    }

    public synchronized void setDelay(int value) {
        this.delay = value;
        resetTimer();
    }

    public int getDelay() {
        return this.delay;
    }

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
                            KioskMain.getUI().setScene(new WelcomeViewController());
                            KioskMain.getLogin().logout();
                        });
            }
        };

        timer.schedule(task, this.delay);
    }

    public synchronized void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
