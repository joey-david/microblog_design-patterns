package fr.univ_lyon1.info.m1.microblog.model;

import fr.univ_lyon1.info.m1.microblog.config.YConfiguration;
import fr.univ_lyon1.info.m1.microblog.controller.Controller;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.io.IOException;

import java.util.concurrent.CountDownLatch;

public class StrategyScoreTest {
    static private Y model;
    static private Controller controller;
    static private JfxView view;
    static private YConfiguration config;

    @BeforeAll
    static void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            try {
                model = new Y();
                view = new JfxView(new Stage(), 800, 800);
                controller = new Controller(model, view);
                config = new YConfiguration(model);
                view.setController(controller);
                view.updateMessageList(model.getSortedMessages());
                config.initialize("src/main/resources/messages/test-messages.txt");
            } catch (IOException e) {
                System.err.println("Error initializing app: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }

    @Test
    void BookmarkScoringScoringOrder() {
        // Set the scoring strategy to BookmarkScoring
        model.setScoringStrategy(new BookmarkScoring());

        // Add messages manually
        model.add(new MessageDecorator("this is about 30 characters long!|2024-11-12 09:30:00"));
        model.add(new MessageDecorator("this 10 chars|2024-11-10 09:30:00"));
        model.add(new MessageDecorator("that's short|2024-11-08 09:30:00"));
        model.add(new MessageDecorator("thus, it begins|2024-11-06 09:30:00"));
        model.add(new MessageDecorator("joey's the most handsome guy in the universe|2024-11-04 09:30:00"));
        model.add(new MessageDecorator("1 + 1 = 3 lol|2024-11-02 09:30:00"));
        model.add(new MessageDecorator("message7|2024-10-31 09:30:00"));
        model.add(new MessageDecorator("message8|2024-10-29 09:30:00"));

        // Bookmark specific messages
        model.bookmarkMessage(model.getMessages().get(2)); // "that's short"
        model.bookmarkMessage(model.getMessages().get(3)); // "thus, it begins"
        model.bookmarkMessage(model.getMessages().get(4)); // "joey's the most handsome guy in the universe"

        // Recompute scores after bookmarking
        model.getScoringStrategy().computeScores(model.getMessages());

        // Verify scores
        assertEquals(2, model.getMessages().get(2).getScore());
        assertEquals(3, model.getMessages().get(3).getScore());
        assertEquals(6, model.getMessages().get(4).getScore());
    }
}
