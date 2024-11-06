package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Map;

/** Interface for scoring. */
public interface ScoringStrategy {
    /** Compute the score for all messages in messagesData. */
    void computeScores(Map<Message, MessageData> messageData);
}
