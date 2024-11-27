package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 *  Test scoring of messages based on relevance.
 */
public class MostRelevantScoringTest {

    @Test
    void testMostRelevantScoring() {
        // Given
        List<MessageDecorator> msgs = new ArrayList<>();

        MessageDecorator m1 = new MessageDecorator("Message 1");
        m1.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(12)));
        m1.setBookmarked(true);
        msgs.add(m1);

        MessageDecorator m2 = new MessageDecorator("This is longer, and it's Message 2");
        m2.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)));
        m2.setBookmarked(false);
        msgs.add(m2);

        MessageDecorator m3 = new MessageDecorator("This is the perfect length,"
                + " it's scoring the top length score.");
        m3.setPublicationDate(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)));
        m3.setBookmarked(true);
        msgs.add(m3);

        // When
        new MostRelevantScoring().computeScores(msgs);

        // Then
        assertThat(m1.getScore(), is(3)); // recent and bookmarked, bad lengths
        assertThat(m2.getScore(), is(8)); // recent but not bookmarked, medium length
        assertThat(m3.getScore(), is(13)); // recent and bookmarked with ideal length
    }
}