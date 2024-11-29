package fr.univ_lyon1.info.m1.microblog.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User of the application.
 */
public class User {
    private String id;
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
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * Default constructor for User.
     * @param id must be a unique identifier.
     */
    public User(final String id) {
        this.id = id;
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
