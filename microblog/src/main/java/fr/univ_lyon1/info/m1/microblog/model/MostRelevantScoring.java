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
     * Compute the score for all messages in messagesData.
     *
     * @param messages List of messages to score.
     */
    @Override
    public void computeScores(final List<MessageDecorator> messages) {
        // Reset scores
        messages.forEach(m -> m.setScore(0));

        // Temporary storage for scores
        int[] tempScores = new int[messages.size()];

        // Apply each strategy and accumulate scores
        for (ScoringStrategy strategy : strategies) {
            strategy.computeScores(messages);
            for (int i = 0; i < messages.size(); i++) {
                tempScores[i] += messages.get(i).getScore();
            }
        }

        // Set the final scores
        for (int i = 0; i < messages.size(); i++) {
            messages.get(i).setScore(tempScores[i]);
        }
    }

    /**
     * Get the list of scoring strategies.
     *
     * @return List of scoring strategies.
     */
    @Override
    public String toString() {
        return "Most Relevant Messages";
    }
}
