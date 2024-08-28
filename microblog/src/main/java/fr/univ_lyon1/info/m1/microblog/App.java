package fr.univ_lyon1.info.m1.microblog;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.Y;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;
import javafx.application.Application;
import javafx.stage.Stage;

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
        JfxView v = new JfxView(y, stage, 600, 600);
        v.addMessage(new Message("Hello"));
        y.createExampleMessages(v);

        // Second view (uncomment to activate)
        // new JfxView(y, new Stage(), 400, 400);
    }

    /**
     * A main method in case the user launches the application using
     * App as the main class.
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }
}
