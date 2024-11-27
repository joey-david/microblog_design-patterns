package fr.univ_lyon1.info.m1.microblog.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 *  Test scoring of messages based on bookmarks.
 */
public class BookmarkScoringTest {

    @Test
    void testBookmarkScoring() {
        //

        List<MessageDecorator> msgs = new ArrayList<>();

        MessageDecorator m1 = new MessageDecorator("Hello world");
        m1.setBookmarked(true);
        msgs.add(m1);

        MessageDecorator m2 = new MessageDecorator("Hello everyone");
        m2.setBookmarked(false);
        msgs.add(m2);

        MessageDecorator m3 = new MessageDecorator("Goodbye world");
        m3.setBookmarked(false);
        msgs.add(m3);

        // When
        new BookmarkScoring().computeScores(msgs);

        // Then
        assertThat(m1.getScore(), is(2)); // "hello" and "world" are bookmarked
        assertThat(m2.getScore(), is(1)); // "hello" is bookmarked
        assertThat(m3.getScore(), is(1)); // "world" is bookmarked
    }
}
