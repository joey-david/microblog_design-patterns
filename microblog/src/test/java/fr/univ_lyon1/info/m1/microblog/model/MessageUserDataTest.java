package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

/**
 *  Test message list manipulations.
 */
public class MessageUserDataTest {
    @Test
    void dummyTestMessageContent() {
        Message m = new Message("Some content");

        String s = m.getContent();

        assertThat(s, is("Some content"));
    }

    @Test
    void testSortMessages() {
        // Given
        List<MessageDecorator> msgs = new ArrayList<>();

        MessageDecorator m1 = new MessageDecorator("Hello, world!");
        add(msgs, m1);
        MessageDecorator m2 = new MessageDecorator("Hello, you!");
        add(msgs, m2);
        MessageDecorator m3 = new MessageDecorator("What is this message?");
        add(msgs, m3);

        User user = new User("testUser");

        // Bookmark a message for the user
        user.toggleMessageBookmark(m1.getMessageId());

        // When
        new BookmarkScoring().computeScores(msgs, user);

        // Then
        assertThat(user.getMessageScore(m1.getMessageId()), is(2));
        assertThat(user.getMessageScore(m2.getMessageId()), is(1));
        assertThat(user.getMessageScore(m3.getMessageId()), is(0));

        List<MessageDecorator> sorted = msgs.stream()
                .sorted((MessageDecorator left, MessageDecorator right) ->
                        Integer.compare(user.getMessageScore(right.getMessageId()),
                                user.getMessageScore(left.getMessageId()))
                )
                .collect(Collectors.toList());

        assertThat(sorted, contains(m1, m2, m3));
    }

    private void add(final List<MessageDecorator> msgs, final MessageDecorator m) {
        msgs.add(m);
    }
}
