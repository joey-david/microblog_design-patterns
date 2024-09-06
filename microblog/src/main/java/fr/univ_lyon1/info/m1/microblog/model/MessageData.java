package fr.univ_lyon1.info.m1.microblog.model;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

/**
 * Piece of data associated to a message for a particular user.
 */
public class MessageData {
    /**
     * Represents the data associated with a message.
     */
    private boolean isBookmarked = false;
    private int score = -1;
    private Set<String> words = new HashSet<>();
    private Date publicationDate = new Date();

    /**
     * Returns the set of words associated with the message.
     *
     * @return the set of words
     */
    public Set<String> getWords() {
        return words;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(final Date date) {
        this.publicationDate = date;
    }

    /**
     * Sets the set of words associated with the message.
     *
     * @param words the set of words
     */
    public void setWords(final Set<String> words) {
        this.words = words;
    }

    /**
     * Returns the score of the message.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the message.
     *
     * @param score the score
     */
    public void setScore(final int score) {
        this.score = score;
    }

    /**
     * Returns whether the message is bookmarked or not.
     *
     * @return true if the message is bookmarked, false otherwise
     */
    public boolean isBookmarked() {
        return isBookmarked;
    }

    /**
     * Sets whether the message is bookmarked or not.
     *
     * @param bookmarked true if the message is bookmarked, false otherwise
     */
    public void setBookmarked(final boolean bookmarked) {
        this.isBookmarked = bookmarked;
    }

    /**
     * Compare two Messages metadata. Suitable for sorting.
     */
    public int compare(final MessageData rightData) {
        int scoreLeft = getScore();
        int scoreRight = rightData.getScore();

        //if either of the messages is bookmarked, place it before the other.
        if (this.isBookmarked()) {
            return 1;
        }
        if (rightData.isBookmarked()) {
            return -1;
        }
        if (scoreLeft < scoreRight) {
            return -1;
        } else if (scoreLeft == scoreRight) {
            return 0;
        } else if (scoreLeft > scoreRight) {
            return 1;
        }
        throw new AssertionError("Case not covered in comparision");
    }
}
