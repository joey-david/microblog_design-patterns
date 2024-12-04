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
    private ScoringStrategy scoringStrategy;
    private MessageFactory messageFactory;

    /** Default constructor with dateScoring Strategy. */
    public Y() {
        this(new MostRelevantScoring());
    }

    /** Constructor for the model, mainly necessary to add a specific scoring strategy. */
    public Y(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
        this.messageFactory = new DefaultMessageFactory();
    }

    /** Setter for the scoring strategy. */
    public void setScoringStrategy(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
        for (User u : users) {
            scoringStrategy.computeScores(messages, u);
        }
        pcs.firePropertyChange("SCORING_STRATEGY_CHANGED", null, null);
    }

    /** Setter for the message factory. */
    public void setMessageFactory(final MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    /** Getter for the scoring strategy. */
    public ScoringStrategy getScoringStrategy() {
        return scoringStrategy;
    }

    /** Create a user and add it to the user's registry. */
    public User createUser(final String id) {
        User u = new User(id);
        users.add(u);
        pcs.firePropertyChange("USER_ADDED", null, u);
        return u;
    }

    /** Create a user and add it to the user's registry. */
    public User createUser(final String id, final String username) {
        User u = new User(id, username);
        users.add(u);
        pcs.firePropertyChange("USER_ADDED", null, u);
        return u;
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
    public void publish(final String content, final String u) { //TODO: implement
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
            scoringStrategy.computeScores(messages, u);
        }
        pcs.firePropertyChange("MESSAGE_ADDED", null, message);
    }

    /** Remove a message. */
    public void removeMessage(final MessageDecorator message) {
        messages.remove(message);
        for (User u : users) {
            scoringStrategy.computeScores(messages, u);
        }
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
                            int scoreDiff = m2.getScore() - m1.getScore();
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
        }
        scoringStrategy.computeScores(messages, user);
        pcs.firePropertyChange("MESSAGE_BOOKMARKED", null, userId);
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
   public int getMessageScore(final MessageDecorator message) {
       return message.getScore();
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
}
