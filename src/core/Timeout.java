package core;

import controllers.WelcomeScreenController;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zack on 4/24/2017.
 */
public class Timeout {
    private KioskMain main;
    private Timer timer = new Timer();

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    main.getUI().setScene(new WelcomeScreenController());
                }
            });
        }
    };

    public Timeout(KioskMain main) {
        this.main = main;
    }

    public void startTimer() {
        System.out.println("Starting timer!");
        timer.schedule(task, 10);
        System.out.println("Task complete");
    }

    public void resetTimer() {
        System.out.println("Resetting timer!");
        timer.cancel();
        timer.schedule(task, 10);
    }
}
