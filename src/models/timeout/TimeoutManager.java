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

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(
                    () -> {
                        System.out.println("1");
                        timer.cancel();
                        KioskMain.getUI().setScene(new WelcomeScreenController());
                        System.out.println("2");
                    });
        }
    };

    public TimeoutManager(){
        timer = new Timer();
    }

    public void startTimer() {
        System.out.println("Starting timer!");
        timer.schedule(task, 15000);
        System.out.println("Task complete");
    }

    public void resetTimer() {
        System.out.println("Resetting timer!");
        timer.cancel();
        timer.purge();
        timer.schedule(task, 30000);
    }
}
