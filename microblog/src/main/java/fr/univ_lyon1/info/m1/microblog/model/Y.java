package fr.univ_lyon1.info.m1.microblog.model;


import java.awt.TextArea;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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
        this(new DateScoring());
    }

    /** Constructor for the model, mainly necessary to add a specific scoring strategy. */
    public Y(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
        this.messageFactory = new DefaultMessageFactory();
    }

    /** Setter for the scoring strategy. */
    public void setScoringStrategy(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
        this.scoringStrategy.computeScores(messages);
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

    /** Get the users in the registry. */
    public Collection<User> getUsers() {
        return users;
    }

    /** Create a message for a specific user, not implemented. */
    public void publish(final TextArea t, final User u) {
        add(new MessageDecorator(t.getText()));
    }

    /** Add method which uses the factory. */
    public void add(final String content) {
        MessageDecorator message = messageFactory.createMessage(content);
        add(message);
    }

    /** Post a message to all users. */
    public void add(final MessageDecorator message) {
        messages.add(message);
        scoringStrategy.computeScores(messages);
        pcs.firePropertyChange("MESSAGE_ADDED", null, message);
    }

    /** Get the messages. */
    public List<MessageDecorator> getMessages() {
        return messages;
    }

   /** Get the sorted messages. */
   public List<MessageDecorator> getSortedMessages() {
       List<MessageDecorator> sortedMessages = new ArrayList<>(messages);
       sortedMessages.sort((MessageDecorator m1, MessageDecorator m2) -> {
            return (-m1.getData().compare(m2.getData()));
       });
       return sortedMessages;
   }


   /** Bookmark the message. */
   public void bookmarkMessage(final MessageDecorator message) {
        message.setBookmarked(!message.isBookmarked());
        scoringStrategy.computeScores(messages);
        pcs.firePropertyChange("MESSAGE_BOOKMARKED", null, message);
   }

   /** Getter for bookmark. */
   public boolean isMessageBookmarked(final MessageDecorator message) {
       return message.isBookmarked();
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
