package fr.univ_lyon1.info.m1.microblog.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.univ_lyon1.info.m1.microblog.view.JfxView;

/**
 * Toplevel class for the Y microbloging application's model.
 */
public class Y {
    private List<User> users = new ArrayList<>();
    private JfxView view;
    public void setView(final JfxView view) {
        this.view = view;
    }

    /** Create a user and add it to the user's registry. */
    public User createUser(final String id) {
        User u = new User(id);
        users.add(u);
        return u;
    }

    /**
     * Create an example set of users and messages, for testing.
     */
    public void createExampleMessages(final JfxView v) {
        User foo = createUser("foo");
        User bar = createUser("bar");
        setView(v);
        view.createUsersPanes();
        Message m1 = new Message("Hello, world!");
        add(m1);
        Message m2 = new Message("What is this message?");
        add(m2);
        add(new Message("Good bye, world!"));
        add(new Message("Hello, you!"));
        add(new Message("Hello hello, world world world."));
    }

    /** Get the users in the registry. */
    public Collection<User> getUsers() {
        return users;
    }


    /** Post a message. */
    public void add(final Message message) {
        view.addMessage(message);
    }
}
