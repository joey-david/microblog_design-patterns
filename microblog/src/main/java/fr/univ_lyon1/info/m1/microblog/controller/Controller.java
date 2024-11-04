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
        if(arg instanceof String) {
            String event = (String) arg;
            switch(event) {
                case "new user":
                    view.updateUserList(model.getUsers());
                    break;
                case "new message":
                case "bookmarked":
                    view.updateMessageList(model.getMessages());
                    break;
            }
        }
    }

    /**
     * Create an example set of users and messages, for testing.
     */
    public void createExampleMessages(final JfxView v) {
        User foo = model.createUser("foo");
        User bar = model.createUser("bar");
        Message m1 = new Message("Hello, world!");
        model.add(m1);
        Message m2 = new Message("What is this message?");
        model.add(m2);
        model.add(new Message("Good bye, world!"));
        model.add(new Message("Hello, you!"));
        model.add(new Message("Hello hello, world world world."));
    }
}

