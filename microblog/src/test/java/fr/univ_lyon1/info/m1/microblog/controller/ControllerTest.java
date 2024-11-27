package fr.univ_lyon1.info.m1.microblog.controller;

import fr.univ_lyon1.info.m1.microblog.model.*;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    static private Controller controller;
    static private Y model;
    static private JfxView view;

    @BeforeAll
    static void setUp() throws Exception {
        /* ??? stolen online, please don't ask us to explain
         * thread wizardry */
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
        ScoringStrategy strategy = new BookmarkScoring();
        controller.switchScoringStrategy(strategy);

        assertEquals(strategy, model.getScoringStrategy());
        assertInstanceOf(BookmarkScoring.class, model.getScoringStrategy());
    }

    @Test
    void testSwitchScoringStrategyChronological() {
        ScoringStrategy strategy = new ChronologicalScoring();
        controller.switchScoringStrategy(strategy);

        assertEquals(strategy, model.getScoringStrategy());
        assertInstanceOf(ChronologicalScoring.class, model.getScoringStrategy());
    }

    @Test
    void TestSwitchScoringStrategyRelevant() {
        ScoringStrategy strategy = new MostRelevantScoring();
        controller.switchScoringStrategy(strategy);

        assertEquals(strategy, model.getScoringStrategy());
        assertInstanceOf(MostRelevantScoring.class, model.getScoringStrategy());
    }

    @Test
    void testCreateUser() {
        controller.createUser("user");

        assertEquals(1, model.getUsers().size());
        assertTrue(model.getUsers().stream().anyMatch(user -> user.getId().equals("user")));
    }

    @Test
    void testPublishMessage() {
        for(MessageDecorator m: model.getMessages()) {
            model.removeMessage(m);
        }
        String message = "This message is used to test publishing";
        controller.publishMessage(message);

        assertTrue(model.getMessages().stream().anyMatch(msg -> msg.getContent().equals(message)));
    }

    @Test
    void testDeleteMessage() {
        MessageDecorator message = new MessageDecorator("This message is used to test deleting");
        model.add(message);
        controller.deleteMessage(message);

        assertFalse(model.getMessages().contains(message));
    }

    @Test
    void testToggleBookmark() {
        MessageDecorator message = new MessageDecorator("This message is used to test toggling bookmark");
        model.add(message);
        controller.toggleBookmark(message);

        assertTrue(model.isMessageBookmarked(message));
    }

    @Test
    void testIsMessageBookmarked() {
        MessageDecorator message = new MessageDecorator("This message is used to test getting bookmark status");
        model.add(message);
        model.bookmarkMessage(message);

        assertTrue(controller.isMessageBookmarked(message));
    }

    @Test
    void testMessageScore() {
        MessageDecorator message = new MessageDecorator("This message is used to test scoring");
        model.add(message);

        assertEquals(model.getMessageScore(message), controller.getMessageScore(message));
    }

}
