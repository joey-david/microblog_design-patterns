package fr.univ_lyon1.info.m1.microblog.controller;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.User;
import fr.univ_lyon1.info.m1.microblog.model.Y;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;

import java.util.Observable;
import java.util.Observer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Class of the Controller of the application.
 */
public class Controller implements PropertyChangeListener {
    private Y model;
    private JfxView view;

    /** Controller of the application. */
    public Controller(final Y model, final JfxView view) {
        this.model = model;
        this.view = view;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        switch (propertyName) {
            case "USER_ADDED":
                view.updateUserList(model.getUsers());
                break;
            case "MESSAGE_ADDED":
            case "MESSAGE_BOOKMARKED":
                view.updateMessageList(model.getSortedMessages(), model.getMessageData());
                break;
            default:
                break;
        }
    }

    /** Calls the model's method to create the user. */
    public void createUser(final String id) {
        model.createUser(id);
    }

    /** Calls the model's method to publish a message. */
    public void publishMessage(final String content) {
        Message message = new Message(content);
        model.add(message);
    }

    /** Calls the model's method to bookmark the message. */
    public void toggleBookmark(final Message message) {
        model.bookmarkMessage(message);
    }

    /** Getter for bookmark. */
    public boolean isMessageBookmarked(final Message message) {
        return model.isMessageBookmarked(message);
    }

    /** Getter for the score. */
    public int getMessageScore(final Message message) {
        return model.getMessageScore(message);
    }

    /**
     * Create an example set of users and messages, for testing.
     */
    public void createExampleMessages() {
        User foo = model.createUser("foo");
        User bar = model.createUser("bar");
        publishMessage("Hello, world!");
        publishMessage("What is this message?");
        publishMessage("Good bye, world!");
        publishMessage("Hello, you!");
        publishMessage("Hello hello, world world world.");
    }
}
