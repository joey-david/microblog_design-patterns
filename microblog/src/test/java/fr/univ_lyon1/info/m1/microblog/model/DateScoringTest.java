package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 *  Test scoring of messages based on dates.
 */
public class DateScoringTest {

    @Test
    void testDateScoring() {
        // Given
        List<MessageDecorator> msgs = new ArrayList<>();

        MessageDecorator m1 = new MessageDecorator("Message 1");
        m1.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12)));
        msgs.add(m1);

        MessageDecorator m2 = new MessageDecorator("Message 2");
        m2.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)));
        msgs.add(m2);

        MessageDecorator m3 = new MessageDecorator("Message 3");
        m3.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(10)));
        msgs.add(m3);

        // When
        new DateScoring().computeScores(msgs);

        // Then
        assertThat(m1.getScore(), is(2)); // within 1 day and 7 days
        assertThat(m2.getScore(), is(1)); // within 7 days
        assertThat(m3.getScore(), is(0)); // older than 7 days
    }
}
