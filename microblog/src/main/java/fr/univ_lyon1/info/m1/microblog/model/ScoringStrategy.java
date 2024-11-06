package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Map;

public interface ScoringStrategy {
    void computeScores(Map<Message, MessageData> messageData);
}
