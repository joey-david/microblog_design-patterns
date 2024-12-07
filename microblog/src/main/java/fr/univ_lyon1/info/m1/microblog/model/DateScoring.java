package fr.univ_lyon1.info.m1.microblog.model;

import java.util.List;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Scoring of messages based on dates.
 */
public class DateScoring implements ScoringStrategy {

    /** Compute the score for all messages in messagesData for a specific user. */
    @Override
    public void computeScores(final List<MessageDecorator> messages, final User user) {
        Date cur = new Date();
        messages.forEach(m -> {
            int score = 0;

            Date publicationDate = m.getPublicationDate();

            long timeDiff = cur.getTime() - publicationDate.getTime();

            if (timeDiff <= TimeUnit.DAYS.toMillis(1)) {
                score++;
            }

            if (timeDiff <= TimeUnit.DAYS.toMillis(7)) {
                score++;
            }

            user.setMessageScore(m.getMessageId(), score);
        });
    }

    @Override
    public String toString() {
        return "Date";
    }
}
