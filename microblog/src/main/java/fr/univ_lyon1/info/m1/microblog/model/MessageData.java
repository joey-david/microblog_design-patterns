package fr.univ_lyon1.info.m1.microblog.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Piece of data associated to a message for a particular user.
 */
public class MessageData {
    /**
     * Represents the data associated with a message.
     */
    private Set<String> words = new HashSet<>();

    /**
     * Returns the set of words associated with the message.
     *
     * @return the set of words
     */
    public Set<String> getWords() {
        return words;
    }

    /**
     * Sets the set of words associated with the message.
     *
     * @param words the set of words
     */
    public void setWords(final Set<String> words) {
        this.words = words;
    }
}
