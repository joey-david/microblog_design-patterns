package fr.univ_lyon1.info.m1.microblog.controller;

import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.User;
import fr.univ_lyon1.info.m1.microblog.model.Y;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;

import java.util.Observable;
import java.util.Observer;


public class Controller implements Observer {
    private Y model;
    private JfxView view;

    public Controller(Y _model, JfxView _view) {
        this.model = _model;
        this.view = _view;
        this.model.addObserver(this);
    }

    public void createUser(String id) {
        model.createUser(id);
    }

    public void publishMessage(String content) {
        Message message = new Message(content);
        model.add(message);
    }

    public void toggleBookmark(Message message) {
        model.bookmarkMessage(message);
    }

    public boolean isMessageBookmarked(Message message) {
        return model.isMessageBookmarked(message);
    }

    public int getMessageScore(Message message) {
        return model.getMessageScore(message);
    }

    @Override
    public void update(Observable o, Object arg) {
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