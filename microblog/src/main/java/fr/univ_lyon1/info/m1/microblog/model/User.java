package fr.univ_lyon1.info.m1.microblog.model;

import java.util.*;

/**
 * User of the application.
 */
public class User {
    private String id;
    private String username;
    private List<UUID> bookmarks = new ArrayList<>();
    private Map<UUID, Integer> messageScores = new HashMap<>();

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


    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

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

    public List<UUID> getBookmarkedMessages() {
        return bookmarks;
    }

    /**
     * Toggle the bookmark of a message.
     * @param messageId the id of the message to toggle the bookmark.
     */
    public void toggleMessageBookmark(final UUID messageId) {
        if (bookmarks.isEmpty()) {
            bookmarks.add(messageId);
            return;
        }
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
        if (bookmarks.isEmpty()) {
            return false;
        }
        return bookmarks.contains(messageId);
    }

    public int getMessageScore(UUID messageId) {
        return messageScores.getOrDefault(messageId, 0);
    }

    public void setMessageScore(UUID messageId, int score) {
        messageScores.put(messageId, score);
    }

    public void removeMessageScore(UUID messageId) {
        messageScores.remove(messageId);
    }
}
