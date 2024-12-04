package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Scoring of messages based on bookmarks.
 */
public class BookmarkScoring implements ScoringStrategy {

    /** Compute the score for all messages in messagesData for a specific user. */
    @Override
    public void computeScores(final List<MessageDecorator> messages, final User user) {
        Set<String> bookmarkedWords = new HashSet<>();

        messages.forEach((MessageDecorator m) -> {
            MessageData d = m.getData();
            String[] w = m.getContent().toLowerCase().split("[^\\p{Alpha}]+");
            Set<String> words = new HashSet<>(Arrays.asList(w));
            d.setWords(words);
            if (user.isMessageBookmarked(m.getMessageId())) {
                bookmarkedWords.addAll(words);
            }
        });

        messages.forEach((MessageDecorator m) -> {
            MessageData d = m.getData();
            int score = 0;
            for (String w : d.getWords()) {
                if (bookmarkedWords.contains(w)) {
                    score++;
                }
            }
            d.setScore(score);
        });
    }

    @Override
    public String toString() {
        return "Messages scored by bookmarks";
    }
}