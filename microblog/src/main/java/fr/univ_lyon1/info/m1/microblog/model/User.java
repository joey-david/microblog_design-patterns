package fr.univ_lyon1.info.m1.microblog.model;

import java.util.*;

/**
 * User of the application.
 */
public class User {
    private String id;
    private String username;
    private Set<UUID> bookmarks = new HashSet<>();
    private Map<UUID, Integer> messageScores = new HashMap<>();
    private ScoringStrategy scoringStrategy;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (id == null) {
            return other.id == null;
        } else {
            return id.equals(other.id);
        }
    }

    /**
     * Default constructor for User.
     * @param id must be a unique identifier.
     */
    public User(final String id) {
        this.id = id;
        this.username = "no name set, id: " + id;
    }

    /**
     * Constructor for User.
     * @param id must be a unique identifier.
     * @param username must be a unique username.
     */
    public User(final String id, final String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * Get the username of the user.
     * @return the username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the user.
     * @param username must be a unique username.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Get the id of the user.
     * @return the id of the user.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the id of the user.
     * @param id must be a unique identifier.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Toggle the bookmark of a message.
     * @param messageId the id of the message to toggle the bookmark.
     */
    public void toggleMessageBookmark(final UUID messageId) {
        if (bookmarks.contains(messageId)) {
            bookmarks.remove(messageId);
        } else {
            bookmarks.add(messageId);
        }
    }

    /**
     * Check if a message is bookmarked.
     * @param messageId the id of the message to check.
     * @return true if the message is bookmarked, false otherwise.
     */
    public boolean isMessageBookmarked(final UUID messageId) {
        return bookmarks.contains(messageId);
    }

    /**
     * Get the score of a message.
     * @param messageId the id of the message to get the score.
     * @return the score of the message.
     */
    public int getMessageScore(final UUID messageId) {
        return messageScores.getOrDefault(messageId, 0);
    }

    /**
     * Set the score of a message.
     * @param messageId the id of the message to set the score.
     * @param score the score to set.
     */
    public void setMessageScore(final UUID messageId, final int score) {
        messageScores.put(messageId, score);
    }

    /**
     * Remove the score of a message.
     * @param messageId the id of the message to remove the score.
     */
    public void removeMessageScore(final UUID messageId) {
        messageScores.remove(messageId);
    }

    /**
     * Get the scoring strategy.
     * @return the scoring strategy.
     */
    public ScoringStrategy getScoringStrategy() {
        return scoringStrategy;
    }

    /**
     * Set the scoring strategy.
     * @param scoringStrategy the scoring strategy to set.
     */
    public void setScoringStrategy(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
    }
}
