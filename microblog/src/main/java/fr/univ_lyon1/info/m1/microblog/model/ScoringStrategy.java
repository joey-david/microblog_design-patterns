package fr.univ_lyon1.info.m1.microblog.model;

import java.util.List;

/** Interface for scoring. */
public interface ScoringStrategy {
    /** Compute the score for all messages in messagesData. */
    void computeScores(List<MessageDecorator> messages);
}
