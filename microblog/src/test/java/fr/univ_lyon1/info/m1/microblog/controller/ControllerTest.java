package fr.univ_lyon1.info.m1.microblog.controller;

import fr.univ_lyon1.info.m1.microblog.model.Y;
import fr.univ_lyon1.info.m1.microblog.model.ScoringStrategy;
import fr.univ_lyon1.info.m1.microblog.model.BookmarkScoring;
import fr.univ_lyon1.info.m1.microblog.model.MessageDecorator;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test the Controller class.
 */
public class ControllerTest {
    private static Controller controller;
    private static Y model;
    private static JfxView view;

    @BeforeAll
    static void setUp() {
        model = new Y();
        view = Mockito.mock(JfxView.class);
        controller = new Controller(model, view);
    }

    @Test
    void testSwitchScoringStrategyBookmark() {
        controller.createUser("user", "testUser");
        ScoringStrategy strategy = new BookmarkScoring();
        controller.switchScoringStrategy(strategy, "user");
        assertEquals(strategy, model.getUserById("user").getScoringStrategy());
        assertInstanceOf(BookmarkScoring.class, model.getUserById("user").getScoringStrategy());
        controller.removeUser("user");
    }

    @Test
    void testCreateUser() {
        controller.createUser("user", "testUser");
        assertEquals(1, model.getUsers().size());
        assertTrue(model.getUsers().stream().anyMatch(user -> user.getId().equals("user")));
        controller.removeUser("user");
    }

    @Test
    void testRemoveUser() {
        controller.createUser("user", "testUser");
        controller.removeUser("user");
        assertEquals(0, model.getUsers().size());
        assertFalse(model.getUsers().stream().anyMatch(user -> user.getId().equals("user")));
    }

    @Test
    void testGetUsernameById() {
        controller.createUser("user", "testUser");
        assertEquals("testUser", controller.getUsernameById("user"));
        controller.removeUser("user");
    }

    @Test
    void testPublishMessage() {
        controller.createUser("user", "testUser");
        for (MessageDecorator m : model.getMessages()) {
            model.removeMessage(m);
        }
        String message = "This message is used to test publishing";
        controller.publishMessage(message, "user");
        assertTrue(model.getMessages().stream().anyMatch(msg -> msg.getContent().equals(message)));
        controller.removeUser("user");
    }

    @Test
    void testDeleteMessage() {
        controller.createUser("user", "testUser");
        MessageDecorator message = new MessageDecorator(
                "This message is used to test deleting",
                "user");
        model.add(message);
        controller.deleteMessage(message, "user");
        assertFalse(model.getMessages().contains(message));
        controller.removeUser("user");
    }

    @Test
    void testToggleBookmark() {
        controller.createUser("user", "testUser");
        MessageDecorator message = new MessageDecorator(
                "This message is used to test toggling bookmark", "user");
        model.add(message);
        controller.toggleBookmark(message, "user");
        assertTrue(model.isMessageBookmarked(message, "user"));
        model.removeMessage(message);
        controller.removeUser("user");
    }

    @Test
    void testIsMessageBookmarked() {
        controller.createUser("user", "testUser");
        MessageDecorator message = new MessageDecorator(
                "This message is used to test getting bookmark status", "user");
        model.add(message);
        model.bookmarkMessage(message, "user");
        assertTrue(controller.isMessageBookmarked(message, "user"));
        model.removeMessage(message);
        controller.removeUser("user");
    }

    @Test
    void testGetMessageScore() {
        controller.createUser("user", "testUser");
        MessageDecorator message = new MessageDecorator(
                "This message is used to test scoring", "user");
        model.add(message);
        assertEquals(model.getMessageScore(message, "user"),
                controller.getMessageScore(message, "user"));
        model.removeMessage(message);
        controller.removeUser("user");
    }

    @Test
    void testSearchMessages() {
        controller.createUser("user", "testUser");
        String searchQuery = "test";
        controller.searchMessages(searchQuery, "user");
        MessageDecorator message = new MessageDecorator(
                "This message is used to test searching", "user");
        MessageDecorator message2 = new MessageDecorator(
                "This message is used to try searching", "user");
        assertTrue(model.getFilteredMessages(searchQuery, "user").stream()
                .allMatch(msg -> msg.getContent().contains(searchQuery)));
        model.removeMessage(message);
        model.removeMessage(message2);
        controller.removeUser("user");
    }

    @Test
    void testShouldDisplay() {
        controller.createUser("user", "testUser");
        MessageDecorator message = new MessageDecorator(
                "This message is used to test display", "user");
        model.add(message);
        assertTrue(controller.shouldDisplay(message, "user", 0));
        model.removeMessage(message);
        controller.removeUser("user");
    }
}
