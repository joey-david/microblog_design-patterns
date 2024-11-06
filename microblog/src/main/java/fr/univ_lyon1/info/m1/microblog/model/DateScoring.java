package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Map;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Scoring of messages based on dates.
 */
public class DateScoring implements ScoringStrategy {

    /** Compute the score for all messages in messagesData. */
    @Override
    public void computeScores(final Map<Message, MessageData> messagesData) {
        Date cur = new Date();
        messagesData.forEach((Message m, MessageData md) -> {
            int score = 0;

            Date publicationDate = m.getPublicationDate();

            long timeDiff = cur.getTime() - publicationDate.getTime();

            if (timeDiff <= TimeUnit.DAYS.toMillis(1)) {
                score++;
            }

            if (timeDiff <= TimeUnit.DAYS.toMillis(7)) {
                score++;
            }

            md.setScore(score);
        });
    }

}
