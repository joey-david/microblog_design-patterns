package fr.univ_lyon1.info.m1.microblog.model;

import java.util.List;

/**
 * Global scoring strategy that combines multiple scoring strategies.
 */
public class MostRelevantScoring implements ScoringStrategy {
    private final List<ScoringStrategy> strategies = List.of(
            new DateScoring(),
            new LengthScoring(),
            new BookmarkScoring());

    /**
     * Compute the score for all messages in messagesData for a specific user.
     *
     * @param messages List of messages to score.
     * @param user     The user for whom the scores are being computed.
     */
    @Override
    public void computeScores(final List<MessageDecorator> messages, final User user) {
        // Reset scores
        messages.forEach(m -> m.setScore(0));

        // Temporary storage for scores
        int[] tempScores = new int[messages.size()];

        // Apply each strategy and accumulate scores
        for (ScoringStrategy strategy : strategies) {
            strategy.computeScores(messages, user);
            for (int i = 0; i < messages.size(); i++) {
                tempScores[i] += messages.get(i).getScore();
            }
        }

        // Set the final scores
        for (int i = 0; i < messages.size(); i++) {
            messages.get(i).setScore(tempScores[i]);
        }
    }

    @Override
    public String toString() {
        return "Most Relevant Messages";
    }
}
