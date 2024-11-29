package fr.univ_lyon1.info.m1.microblog;

import fr.univ_lyon1.info.m1.microblog.config.YConfiguration;
import fr.univ_lyon1.info.m1.microblog.controller.Controller;
import fr.univ_lyon1.info.m1.microblog.model.Y;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the application (structure imposed by JavaFX).
 */
public class App extends Application {

    /**
     * With javafx, start() is called when the application is launched.
     */
    @Override
    public void start(final Stage stage) throws Exception {
        final Y y = new Y();
        YConfiguration config = new YConfiguration(y);
        JfxView v = new JfxView(stage, 600, 600);
        Controller controller = new Controller(y, v);
        v.setController(controller);
        for(String userId : y.getUserIds()) {
            v.updateMessageListForUser(y.getSortedMessages(userId), userId);
        }

        // Second view (uncomment to activate)
//        JfxView v2 = new JfxView(new Stage(), 400, 400);
//        Controller controller2 = new Controller(y, v2);
//        v2.setController(controller2);

        try {
            config.initialize();
        } catch (IOException e) {
            System.err.println("Error initializing app: " + e.getMessage());
        }
    }

    /**
     * A main method in case the user launches the application using
     * App as the main class.
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }
}
