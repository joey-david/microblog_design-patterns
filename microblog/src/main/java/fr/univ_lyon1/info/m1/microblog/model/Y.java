package fr.univ_lyon1.info.m1.microblog.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Collection;

/**
 * Toplevel class for the Y microbloging application's model.
 */
public class Y extends Observable {
    private List<User> users = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private Map<Message, MessageData> messageData = new LinkedHashMap<>();
    private BookmarkScoring bookmarkScoring = new BookmarkScoring();

    /** Create a user and add it to the user's registry. */
    public User createUser(final String id) {
        User u = new User(id);
        users.add(u);
        setChanged();
        notifyObservers("USER_ADDED");
        return u;
    }

    /** Get the users in the registry. */
    public Collection<User> getUsers() {
        return users;
    }


    /** Post a message. */
    public void add(final Message message) {
        messages.add(message);
        messageData.put(message, new MessageData());
        bookmarkScoring.computeScores(messageData);
        setChanged();
        notifyObservers("MESSAGE_ADDED");
    }

    /** Get the messages. */
   public List<Message> getMessages() {
        return messages;
   }

   /** Bookmark the message. */
   public void bookmarkMessage(final Message message) {
       MessageData data = messageData.get(message);
       if (data != null) {
           data.setBookmarked(!data.isBookmarked());
           bookmarkScoring.computeScores(messageData);
           setChanged();
           notifyObservers("MESSAGE_BOOKMARKED");
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
