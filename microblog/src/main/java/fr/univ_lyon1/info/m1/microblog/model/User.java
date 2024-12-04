package fr.univ_lyon1.info.m1.microblog.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User of the application.
 */
public class User {
    private String id;
    private String username;
    private List<UUID> bookmarks = new ArrayList<>();

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
        } else return id.equals(other.id);
    }

    /**
     * Default constructor for User.
     * @param id must be a unique identifier.
     */
    public User(final String id) {
        this.id = id;
        this.username = "no name set, id: " + id;
    }

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

    public void setId(final String id) {
        this.id = id;
    }

    public List<UUID> getBookmarkedMessages() {
        return bookmarks;
    }

    public void toggleMessageBookmark(final UUID messageId) {
        if(bookmarks.isEmpty()) {
            bookmarks.add(messageId);
            return;
        }
        if (bookmarks.contains(messageId)) {
            bookmarks.remove(messageId);
        } else {
            bookmarks.add(messageId);
        }
    }

    public boolean isMessageBookmarked(final UUID messageId) {
        if(bookmarks.isEmpty()) {
            return false;
        }
        return bookmarks.contains(messageId);
    }
}
