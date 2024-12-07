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
 *  Test scoring of messages based on dates.
 */
public class DateScoringTest {

    @Test
    void testDateScoring() {
        // Given
        List<MessageDecorator> msgs = new ArrayList<>();

        MessageDecorator m1 = new MessageDecorator("Message 1", UUID.randomUUID().toString());
        m1.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12)));
        msgs.add(m1);

        MessageDecorator m2 = new MessageDecorator("Message 2", UUID.randomUUID().toString());
        m2.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)));
        msgs.add(m2);

        MessageDecorator m3 = new MessageDecorator("Message 3", UUID.randomUUID().toString());
        m3.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)));
        msgs.add(m3);

        User user = new User(UUID.randomUUID().toString(), "testUser");

        // When
        new DateScoring().computeScores(msgs, user);

        // Then
        assertThat(user.getMessageScore(m1.getMessageId()), is(2)); // within 1 day and 7 days
        assertThat(user.getMessageScore(m2.getMessageId()), is(1)); // within 7 days
        assertThat(user.getMessageScore(m3.getMessageId()), is(0)); // older than 7 days
    }
}
