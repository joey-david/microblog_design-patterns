package fr.univ_lyon1.info.m1.microblog.model;

import java.util.List;


/** Scoring strategy for recent relevant messages. */
public class RecentRelevantScoring implements ScoringStrategy {
    private final List<ScoringStrategy> strategies = List.of(
            new DateScoring(),
            new BookmarkScoring());

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

    @Override
    public String toString() {
        return "Recent Relevant Messages";
    }
}
