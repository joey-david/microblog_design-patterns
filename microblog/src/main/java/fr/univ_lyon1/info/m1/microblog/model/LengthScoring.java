package fr.univ_lyon1.info.m1.microblog.model;

import java.util.List;

/**
 * Scoring of messages based on length.
 * Adds 2 points if the message is 15-close to 42 characters.
 * Adds 1 point if the message is 30-close to 42 characters.
 */
public class LengthScoring implements ScoringStrategy {

    /** Compute the score for all messages in messagesData. */
    @Override
    public void computeScores(final List<MessageDecorator> messages) {
        messages.forEach(m -> {
            int lengthDiscrepancy = Math.abs(42 - m.getContent().length());

            int score = 0;
            if (lengthDiscrepancy <= 15) {
                score = 2;
            } else if (lengthDiscrepancy <= 30) {
                score = 1;
            }

            m.setScore(score);
        });
    }
}
