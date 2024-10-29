package fr.univ_lyon1.info.m1.microblog.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;


/**
 * Toplevel class for the Y microbloging application's model.
 */
public class Y extends Observable {
    private List<User> users = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    /** Create a user and add it to the user's registry. */
    public User createUser(final String id) {
        User u = new User(id);
        users.add(u);
        setChanged();
        notifyObservers("new user");
        return u;
    }

    /** Get the users in the registry. */
    public Collection<User> getUsers() {
        return users;
    }


    /** Post a message. */
    public void add(final Message message) {
        messages.add(message);
        setChanged();
        notifyObservers("new message");
    }

    /** Get the messages */
   public List<Message> getMessages() {
        return messages;
   }
}
