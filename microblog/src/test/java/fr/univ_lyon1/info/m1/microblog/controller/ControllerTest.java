package fr.univ_lyon1.info.m1.microblog.controller;

import fr.univ_lyon1.info.m1.microblog.model.Y;
import fr.univ_lyon1.info.m1.microblog.model.ScoringStrategy;
import fr.univ_lyon1.info.m1.microblog.model.BookmarkScoring;
import fr.univ_lyon1.info.m1.microblog.model.MessageDecorator;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test of Controller methods.
 */
public class ControllerTest {
    private static Controller controller;
    private static Y model;
    private static JfxView view;

    @BeforeAll
    static void setUp() throws Exception {
        /* stolen online lol */
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            try {
                model = new Y();
                view = new JfxView(new Stage(), 800, 800);
                controller = new Controller(model, view);
                view.setController(controller);
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }

    @Test
    void testSwitchScoringStrategyBookmark() {
        // same here, thread wizardry
        Platform.runLater(() -> {
            controller.createUser("user", "testUser");
            ScoringStrategy strategy = new BookmarkScoring();
            controller.switchScoringStrategy(strategy, "user");
            assertEquals(strategy, model.getUserById("user").getScoringStrategy());
            assertInstanceOf(BookmarkScoring.class, model.getUserById("user").getScoringStrategy());
        });
    }

    @Test
    void testCreateUser() {
        Platform.runLater(() -> {
            controller.createUser("user", "testUser");

            assertEquals(1, model.getUsers().size());
            assertTrue(model.getUsers().stream().anyMatch(user -> user.getId().equals("user")));
        });
    }

    @Test
    void testGetUsernameById() {
        Platform.runLater(() -> {
            controller.createUser("user", "testUser");

            assertEquals("testUser", controller.getUsernameById("user"));
        });
    }

    @Test
    void testPublishMessage() {
        Platform.runLater(() -> {
            for (MessageDecorator m : model.getMessages()) {
                model.removeMessage(m);
            }
            String message = "This message is used to test publishing";
            controller.publishMessage(message, "user");

            assertTrue(model.getMessages().stream().
                    anyMatch(msg -> msg.getContent().equals(message)));
        });
    }

    @Test
    void testDeleteMessage() {
        Platform.runLater(() -> {
            MessageDecorator message = new MessageDecorator(
                    "This message is used to test deleting", "user");
            model.add(message);
            controller.deleteMessage(message, "user");

            assertFalse(model.getMessages().contains(message));
        });
    }

    @Test
    void testToggleBookmark() {
        Platform.runLater(() -> {
            MessageDecorator message = new MessageDecorator(
                    "This message is used to test toggling bookmark", "user");
            model.add(message);
            controller.toggleBookmark(message, "user");

            assertTrue(model.isMessageBookmarked(message, "user"));
        });
    }

    @Test
    void testIsMessageBookmarked() {
        Platform.runLater(() -> {
            MessageDecorator message = new MessageDecorator(
                    "This message is used to test getting bookmark status", "user");
            model.add(message);
            model.bookmarkMessage(message, "user");

            assertTrue(controller.isMessageBookmarked(message, "user"));
        });
    }

    @Test
    void testGetMessageScore() {
        Platform.runLater(() -> {
            MessageDecorator message = new MessageDecorator(
                    "This message is used to test scoring", "user");
            model.add(message);

            assertEquals(model.getMessageScore(message, "user"),
                    controller.getMessageScore(message, "user"));
        });
    }

    @Test
    void testSearchMessages() {
        Platform.runLater(() -> {
            String searchQuery = "test";
            controller.searchMessages(searchQuery, "user");

            assertTrue(model.getFilteredMessages(searchQuery, "user").stream()
                    .allMatch(msg -> msg.getContent().contains(searchQuery)));
        });
    }

    @Test
    void testShouldDisplay() {
        Platform.runLater(() -> {
            MessageDecorator message = new MessageDecorator(
                    "This message is used to test display", "user");
            model.add(message);

            assertTrue(controller.shouldDisplay(message, "user", 0));
        });
    }
}
