package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 *  Test scoring of messages based on length.
 */
public class LengthScoringTest {

    @Test
    void testLengthScoring() {
        // Given
        List<MessageDecorator> msgs = new ArrayList<>();

        MessageDecorator m1 = new MessageDecorator("Short message");
        msgs.add(m1);

        MessageDecorator m2 = new MessageDecorator("This is a bit longer message");
        msgs.add(m2);

        MessageDecorator m3 = new MessageDecorator("This is a significantly "
                + "longer message that scores 2");
        msgs.add(m3);

        MessageDecorator m4 = new MessageDecorator("But you see, let me tell you about this:"
                + "this message is way too long, no one wants to read it!");
        msgs.add(m4);

        // When
        new LengthScoring().computeScores(msgs);

        // Then
        assertThat(m1.getScore(), is(0)); // Short message
        assertThat(m2.getScore(), is(1)); // Bit longer message
        assertThat(m3.getScore(), is(2)); // Significantly longer message
        assertThat(m4.getScore(), is(0)); // Way too long message
    }
}
