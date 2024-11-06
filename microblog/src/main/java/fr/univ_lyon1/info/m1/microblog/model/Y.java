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
    private List<Message> messages = new ArrayList<>();
    private Map<Message, MessageData> messageData = new LinkedHashMap<>();
    private ScoringStrategy scoringStrategy;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public Y(ScoringStrategy scoringStrategy) {
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
        add(new Message(t.getText()));
    }


    /** Post a message to all users. */
    public void add(final Message message) {
        messages.add(message);
        messageData.put(message, new MessageData());
        scoringStrategy.computeScores(messageData);
        pcs.firePropertyChange("MESSAGE_ADDED", null, message);
    }

    /** Get the messages. */
   public List<Message> getMessages() {
        return messages;
   }

   /** Get the sorted messages. */
   public List<Message> getSortedMessages() {
       List<Message> sortedMessages = new ArrayList<>(messages);
       sortedMessages.sort((Message m1, Message m2) -> {
           MessageData md1 = messageData.get(m1);
           MessageData md2 = messageData.get(m2);
           return -md1.compare(md2);
       });
       return sortedMessages;
   }

   /** Bookmark the message. */
   public void bookmarkMessage(final Message message) {
       MessageData data = messageData.get(message);
       if (data != null) {
           data.setBookmarked(!data.isBookmarked());
           scoringStrategy.computeScores(messageData);
           pcs.firePropertyChange("MESSAGE_BOOKMARKED", null, message);
       }
   }

   /** Getter for bookmark. */
   public boolean isMessageBookmarked(final Message message) {
       MessageData data = messageData.get(message);
       return data != null && data.isBookmarked();
   }

   /** Getter for score. */
   public int getMessageScore(final Message message) {
       MessageData data = messageData.get(message);
       return data != null ? data.getScore() : -1;
   }

   /** Get the message data. */
   public Map<Message, MessageData> getMessageData() {
       return messageData;
   }
}
