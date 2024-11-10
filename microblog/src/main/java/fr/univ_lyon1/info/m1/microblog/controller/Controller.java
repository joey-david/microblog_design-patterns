package fr.univ_lyon1.info.m1.microblog.controller;

import fr.univ_lyon1.info.m1.microblog.model.MessageDecorator;
import fr.univ_lyon1.info.m1.microblog.model.Y;
import fr.univ_lyon1.info.m1.microblog.view.JfxView;
import fr.univ_lyon1.info.m1.microblog.model.ScoringStrategy;

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
    public void propertyChange(final PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        switch (propertyName) {
            case "USER_ADDED":
                view.updateUserList(model.getUsers());
                break;
            case "MESSAGE_ADDED":
            case "SCORING_STRATEGY_CHANGED":
            case "MESSAGE_BOOKMARKED":
                view.updateMessageList(model.getSortedMessages());
                break;
            default:
                break;
        }
    }

    /** Calls the model's method to switch the scoring strategy. */
    public void switchScoringStrategy(final ScoringStrategy strategy) {
        model.setScoringStrategy(strategy);
    }

    /** Calls the model's method to create the user. */
    public void createUser(final String id) {
        model.createUser(id);
    }

    /** Calls the model's method to publish a message. */
    public void publishMessage(final String content) {
        model.add(content);
    }

    /** Calls the model's method to bookmark the message. */
    public void toggleBookmark(final MessageDecorator message) {
        model.bookmarkMessage(message);
    }

    /** Getter for bookmark. */
    public boolean isMessageBookmarked(final MessageDecorator message) {
        return model.isMessageBookmarked(message);
    }

    /** Getter for the score. */
    public int getMessageScore(final MessageDecorator message) {
        return model.getMessageScore(message);
    }
}
