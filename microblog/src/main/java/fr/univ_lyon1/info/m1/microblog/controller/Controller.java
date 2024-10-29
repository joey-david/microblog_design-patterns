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

    public void publishMessage(String content, String userId) {
        Message message = new Message(content, userId);
        model.add(message);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    /**
     * Create an example set of users and messages, for testing.
     */
    public void createExampleMessages(final JfxView v) {
        User foo = model.createUser("foo");
        User bar = model.createUser("bar");
        Message m1 = new Message("Hello, world!", foo.getId());
        model.add(m1);
        Message m2 = new Message("What is this message?", bar.getId());
        model.add(m2);
        model.add(new Message("Good bye, world!", foo.getId()));
        model.add(new Message("Hello, you!", bar.getId()));
        model.add(new Message("Hello hello, world world world.", foo.getId()));
    }
}

