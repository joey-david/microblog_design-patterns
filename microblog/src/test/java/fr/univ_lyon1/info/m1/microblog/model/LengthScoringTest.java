package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 *  Test scoring of messages based on length.
 */
public class LengthScoringTest {

    @Test
    void testLengthScoring() {
        // Given
        List<MessageDecorator> msgs = new ArrayList<>();

        MessageDecorator m1 = new MessageDecorator("Short message", UUID.randomUUID().toString());
        msgs.add(m1);

        MessageDecorator m2 = new MessageDecorator("This is a bit longer message",
                UUID.randomUUID().toString());
        msgs.add(m2);

        MessageDecorator m3 = new MessageDecorator("This is a significantly longer"
                + " message that scores 2", UUID.randomUUID().toString());
        msgs.add(m3);

        MessageDecorator m4 = new MessageDecorator("But you see, let me tell you about"
                + " this: this message is way too long, no one wants to read it!",
                UUID.randomUUID().toString());
        msgs.add(m4);

        User user = new User(UUID.randomUUID().toString(), "testUser");

        // When
        new LengthScoring().computeScores(msgs, user);

        // Then
        assertThat(user.getMessageScore(m1.getMessageId()), is(0)); // Short message
        assertThat(user.getMessageScore(m2.getMessageId()), is(1)); // Bit longer message
        assertThat(user.getMessageScore(m3.getMessageId()), is(2)); // Significantly longer message
        assertThat(user.getMessageScore(m4.getMessageId()), is(0)); // Way too long message
    }
}
