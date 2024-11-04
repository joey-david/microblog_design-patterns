package fr.univ_lyon1.info.m1.microblog.controller;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.User;
import fr.univ_lyon1.info.m1.microblog.model.Y;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;

import java.util.Observable;
import java.util.Observer;

/**
 * Class of the Controller of the application.
 */
public class Controller implements Observer {
    private Y model;
    private JfxView view;

    /** Controller of the application. */
    public Controller(final Y model, final JfxView view) {
        this.model = model;
        this.view = view;
        this.model.addObserver(this);
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

    @Override
    public void update(final Observable o, final Object arg) {
        if (arg instanceof String) {
            String event = (String) arg;
            switch (event) {
                case "USER_ADDED":
                    view.updateUserList(model.getUsers());
                    break;
                case "MESSAGE_ADDED":
                case "MESSAGE_BOOKMARKED":
                    view.updateMessageList(model.getMessages(), model.getMessageData());
                    break;
                default:
                    break;
            }
        }
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
