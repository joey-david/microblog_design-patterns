package fr.univ_lyon1.info.m1.microblog.model;


import java.awt.TextArea;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
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

    /** Constructor for the model, mainly necessary to add a specific scoring strategy. */
    public Y(final ScoringStrategy scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
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

    /** Post a message to all users. */
    public void add(final MessageDecorator message) {
        messages.add(message);
        scoringStrategy.computeScores(messages);
        pcs.firePropertyChange("MESSAGE_ADDED", null, message);
    }

    /** Get the messages. */
    public List<MessageDecorator> getMessages() { return messages; }

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

   /** Get the message data. */
   public Map<Message, MessageData> getMessageData() {
       Map<Message, MessageData> map = new LinkedHashMap<>();
       for (MessageDecorator md : messages) {
           map.put(md, md.getData());
       }
       return map;
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
