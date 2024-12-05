package fr.univ_lyon1.info.m1.microblog.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Scoring strategy for the bookmarks - focuses on the wordkeeping system.
 */
public class BookmarkScoring implements ScoringStrategy {

    @Override
    public void computeScores(final List<MessageDecorator> messages, final User user) {
        Set<String> bookmarkedWords = new HashSet<>();

        messages.forEach((MessageDecorator m) -> {
            MessageData d = m.getData();
            Set<String> words =
                    new HashSet<>(List.of(m.getContent().toLowerCase().split("[^\\p{Alpha}]+")));
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
            user.setMessageScore(m.getMessageId(), score);
        });
    }

    @Override
    public String toString() {
        return "Messages scored by bookmarks";
    }
}
