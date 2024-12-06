package fr.univ_lyon1.info.m1.microblog.model;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.stream.Collectors;

/**
 * Toplevel class for the Y microbloging application's model.
 */
public class Y {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private List<User> users = new ArrayList<>();
    private List<MessageDecorator> messages = new ArrayList<>();
    private MessageFactory messageFactory;

    /** Default constructor. */
    public Y() {
        this.messageFactory = new DefaultMessageFactory();
    }

    /** Setter for the message factory. */
    public void setMessageFactory(final MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    /**
     * Create a user and add it to the user's registry.
     */
    public void createUser(final String id) {
        User u = new User(id);
        users.add(u);
        pcs.firePropertyChange("USER_ADDED", null, u);
    }

    /**
     * Create a user and add it to the user's registry.
     */
    public void createUser(final String id, final String username) {
        User u = new User(id, username);
        users.add(u);
        pcs.firePropertyChange("USER_ADDED", null, u);
    }

    /** Get the users in the registry. */
    public Collection<User> getUsers() {
        return users;
    }

    /** Get the ids of the users. */
    public List<String> getUserIds() {
        List<String> userIds = new ArrayList<>();
        for (User u : users) {
            userIds.add(u.getId());
        }
        return userIds;
    }

    /** Create a message for a specific user, not implemented. */
    public void publish(final String content, final String u) {
        MessageDecorator message = new MessageDecorator(content, u);
        add(message);
    }

    /** Add method which uses the factory. */
    public void add(final String content) {
        MessageDecorator message = messageFactory.createMessage(content);
        add(message);
    }

    /** Post a message to all users. */
    public void add(final MessageDecorator message) {
        messages.add(message);
        for (User u : users) {
            u.getScoringStrategy().computeScores(messages, u);
        }
        pcs.firePropertyChange("MESSAGE_ADDED", null, message);
    }

    /** search boolean function. */
    private boolean isValidLookup(final String message, final String query) {
        return message.toLowerCase().contains(query.toLowerCase());
    }

    /** Get the filtered messages. */
    public List<MessageDecorator> getFilteredMessages(final String search, final String userId) {
        return getSortedMessages(userId).stream()
                .filter(message -> isValidLookup(message.getContent(), search))
                .collect(Collectors.toList());
    }

    /** Remove a message. */
    public void removeMessage(final MessageDecorator message) {
        for (User u : users) {
            u.removeMessageScore(message.getMessageId());
            if (u.isMessageBookmarked(message.getMessageId())) {
                u.toggleMessageBookmark(message.getMessageId());
            }
            u.getScoringStrategy().computeScores(messages, u);
        }
        messages.remove(message);
        pcs.firePropertyChange("MESSAGE_REMOVED", null, message);
    }

    /** Get the messages. */
    public List<MessageDecorator> getMessages() {
        return messages;
    }

    /** Get the sorted messages. */
    public List<MessageDecorator> getSortedMessages(final String userId) {
        User user = getUserById(userId);
        if (user != null) {
            return messages.stream()
                    .sorted((m1, m2) -> {
                        if (user.isMessageBookmarked(m1.getMessageId())
                                && !user.isMessageBookmarked(m2.getMessageId())) {
                            return -1;
                        } else if (!user.isMessageBookmarked(m1.getMessageId())
                                && user.isMessageBookmarked(m2.getMessageId())) {
                            return 1;
                        } else {
                            int scoreDiff = user.getMessageScore(m2.getMessageId())
                                    - user.getMessageScore(m1.getMessageId());
                            if (scoreDiff == 0) {
                                return m2.getPublicationDate().compareTo(m1.getPublicationDate());
                            } else {
                                return scoreDiff;
                            }
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /** Get the user by id. */
    public User getUserById(final String userId) {
        for (User u : users) {
            if (u.getId().equals(userId)) {
                return u;
            }
        }
        return null;
    }

    /** Set the scoring strategy for a user. */
    public void setScoringStrategy(final ScoringStrategy scoringStrategy, final String userId) {
        User user = getUserById(userId);
        if (user != null) {
            user.setScoringStrategy(scoringStrategy);
            scoringStrategy.computeScores(messages, user);
            pcs.firePropertyChange("SCORING_STRATEGY_CHANGED", null, userId);
        }
    }

    /** Get the username by id. */
    public String getUsernameById(final String userId) {
        User user = getUserById(userId);
        if (user != null) {
            return user.getUsername();
        } else {
            return null;
        }
    }

    /** Bookmark the message. */
    public void bookmarkMessage(final MessageDecorator message, final String userId) {
        User user = getUserById(userId);
        if (user != null) {
            user.toggleMessageBookmark(message.getMessageId());
            user.getScoringStrategy().computeScores(messages, user);
            pcs.firePropertyChange("MESSAGE_BOOKMARKED", null, userId);
        }
    }

   /** Getter for bookmark. */
   public boolean isMessageBookmarked(final MessageDecorator message, final String userId) {
       User user = getUserById(userId);
         if (user != null) {
              return user.isMessageBookmarked(message.getMessageId());
         } else {
              return false;
         }
   }

   /** Getter for score. */
   public int getMessageScore(final MessageDecorator message, final String userId) {
         User user = getUserById(userId);
         if (user != null) {
              return user.getMessageScore(message.getMessageId());
         } else {
              return 0;
         }
   }

   /** Load messages from file. */
   public void loadMessagesFromFile(final String resourcePath) throws IOException {
       List<MessageDecorator> loadedMessages = messageFactory.loadMessagesFromFile(resourcePath);
       for (MessageDecorator md : loadedMessages) {
           add(md);
       }
   }

    /** Add a listener to the class. */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /** Remove the listener. */
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /** Check if a message should be displayed.*/
    public boolean shouldDisplay(final MessageDecorator message,
                                 final String userId, final int threshold) {
        User user = getUserById(userId);
        if (user != null) {
            return user.getMessageScore(message.getMessageId()) >= threshold;
        } else {
            return false;
        }
    }
}
