package fr.univ_lyon1.info.m1.microblog.model;

import java.util.List;

/** Scoring strategy for recent relevant messages. */
public class RecentRelevantScoring implements ScoringStrategy {
    private final List<ScoringStrategy> strategies = List.of(
            new DateScoring(),
            new BookmarkScoring());

    @Override
    public void computeScores(final List<MessageDecorator> messages, final User user) {
        // Reset scores
        messages.forEach(m -> user.setMessageScore(m.getMessageId(), 0));

        // Temporary storage for scores
        int[] tempScores = new int[messages.size()];

        // Apply each strategy and accumulate scores
        for (ScoringStrategy strategy : strategies) {
            strategy.computeScores(messages, user);
            for (int i = 0; i < messages.size(); i++) {
                tempScores[i] += user.getMessageScore(messages.get(i).getMessageId());
            }
        }

        // Set the final scores
        for (int i = 0; i < messages.size(); i++) {
            user.setMessageScore(messages.get(i).getMessageId(), tempScores[i]);
        }
    }

    @Override
    public String toString() {
        return "Recent Relevant Messages";
    }
}