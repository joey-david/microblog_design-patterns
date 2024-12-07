package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 *  Test scoring of messages based on bookmarks.
 */
public class BookmarkScoringTest {

    @Test
    void testBookmarkScoring() {
        // Given
        List<MessageDecorator> msgs = new ArrayList<>();

        MessageDecorator m1 = new MessageDecorator("Hello world", UUID.randomUUID().toString());
        MessageDecorator m2 = new MessageDecorator("Hello everyone", UUID.randomUUID().toString());
        MessageDecorator m3 = new MessageDecorator("Goodbye world", UUID.randomUUID().toString());

        msgs.add(m1);
        msgs.add(m2);
        msgs.add(m3);

        User user = new User(UUID.randomUUID().toString(), "testUser");

        // Bookmark messages for the user
        user.toggleMessageBookmark(m1.getMessageId());
        user.toggleMessageBookmark(m3.getMessageId());

        // When
        new BookmarkScoring().computeScores(msgs, user);

        // Then
        assertThat(user.getMessageScore(m1.getMessageId()), is(2));
        assertThat(user.getMessageScore(m2.getMessageId()), is(1));
        assertThat(user.getMessageScore(m3.getMessageId()), is(2));
    }
}
