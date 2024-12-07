package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 *  Test scoring of messages based on recency and relevance.
 */
public class RecentRelevantScoringTest {

    @Test
    void testRecentRelevantScoring() {
        // Given
        List<MessageDecorator> msgs = new ArrayList<>();

        MessageDecorator m1 = new MessageDecorator("Message 1", UUID.randomUUID().toString());
        m1.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12)));
        msgs.add(m1);

        MessageDecorator m2 = new MessageDecorator("This is longer, and it's Message 2",
                UUID.randomUUID().toString());
        m2.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)));
        msgs.add(m2);

        MessageDecorator m3 = new MessageDecorator("This is the perfect length,"
                + " it's scoring the top length score.", UUID.randomUUID().toString());
        m3.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)));
        msgs.add(m3);

        User user = new User(UUID.randomUUID().toString(), "testUser");

        // Bookmark messages for the user
        user.toggleMessageBookmark(m1.getMessageId());
        user.toggleMessageBookmark(m3.getMessageId());

        // When
        new RecentRelevantScoring().computeScores(msgs, user);

        // Then
        assertThat(user.getMessageScore(m1.getMessageId()), is(3));
        assertThat(user.getMessageScore(m2.getMessageId()), is(5));
        assertThat(user.getMessageScore(m3.getMessageId()), is(12));
    }
}
