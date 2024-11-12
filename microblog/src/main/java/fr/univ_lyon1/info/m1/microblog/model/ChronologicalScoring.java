package fr.univ_lyon1.info.m1.microblog.model;

import java.util.List;
import java.util.Comparator;

/**
 * Scoring of messages in chronological order.
 */
public class ChronologicalScoring implements ScoringStrategy {

    /** Compute the score for all messages in messagesData. */
    @Override
    public void computeScores(final List<MessageDecorator> messages) {
        messages.sort(Comparator.comparing(Message::getPublicationDate).reversed());
        int score = messages.size();
        for (MessageDecorator message : messages) {
            message.setScore(score--);
        }
    }

    @Override
    public String toString() {
        return "Messages in chronological order";
    }
}
