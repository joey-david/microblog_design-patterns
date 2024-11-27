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
        MessageDecorator m3 = new MessageDecorator("What is this message ?");
        add(msgs, m3);
        msgs.get(msgs.indexOf(m1)).setBookmarked(true);

        // When
        new BookmarkScoring().computeScores(msgs);

        // Then
        assertThat(msgs.get(msgs.indexOf(m1)).getScore(), is(2));
        assertThat(msgs.get(msgs.indexOf(m2)).getScore(), is(1));
        assertThat(msgs.get(msgs.indexOf(m3)).getScore(), is(0));

        List<Message> sorted = msgs.stream()
                .sorted((MessageDecorator left, MessageDecorator right) ->
                        Integer.compare(right.getScore(), left.getScore())
                )
                .collect(Collectors.toList());

        assertThat(sorted, contains(m1, m2, m3));

    }

    private void add(final List<MessageDecorator> msgs, final MessageDecorator m) {
        msgs.add(m);
    }
}
