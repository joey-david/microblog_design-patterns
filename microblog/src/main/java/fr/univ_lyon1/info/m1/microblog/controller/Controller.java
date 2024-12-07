package fr.univ_lyon1.info.m1.microblog.controller;

import fr.univ_lyon1.info.m1.microblog.view.JfxView;
import fr.univ_lyon1.info.m1.microblog.model.ScoringStrategy;
import fr.univ_lyon1.info.m1.microblog.model.MessageDecorator;
import fr.univ_lyon1.info.m1.microblog.model.Y;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Class of the Controller of the application.
 */
public class Controller implements PropertyChangeListener {
    private Y model;
    private JfxView view;
    private String currentSearchQuery = "";

    /** Controller of the application. */
    public Controller(final Y model, final JfxView view) {
        this.model = model;
        this.view = view;
        model.addPropertyChangeListener(this);
    }

    /** Calls the model's method to add a property change listener. */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        switch (propertyName) {
            case "USER_ADDED":
            case "USER_REMOVED":
                view.updateUserList(model.getUserIds());
                refreshMessages();
                break;
            case "SCORING_STRATEGY_CHANGED":
            case "MESSAGE_BOOKMARKED":
                view.updateMessageListForUser(model.getSortedMessages((String) evt.getNewValue()),
                        (String) evt.getNewValue());
                break;
            case "MESSAGE_ADDED":
            case "MESSAGE_REMOVED":
                for (String userId : model.getUserIds()) {
                    view.updateMessageListForUser(model.getSortedMessages(userId), userId);
                }
                refreshMessages();
                break;
            default:
                break;
        }
    }

    /** Refreshes the displayed messages. */
    private void refreshMessages() {
        for (String userId : model.getUserIds()) {
            searchMessages(currentSearchQuery, userId);
        }
    }

    /** Calls the model's method to switch the scoring strategy. */
    public void switchScoringStrategy(final ScoringStrategy strategy, final String userId) {
        model.setScoringStrategy(strategy, userId);
        view.updateMessageListForUser(model.getSortedMessages(userId), userId);
    }

    /** Calls the model's method to create the user. */
    public void createUser(final String id, final String username) {
        model.createUser(id, username);
    }

    /** Calls the model's method to remove the user. */
    public void removeUser(final String userId) {
        model.removeUser(userId);
    }

    /** Calls the model's method to get the username by id. */
    public String getUsernameById(final String userId) {
        return model.getUsernameById(userId);
    }

    /** Calls the model's method to publish a message. */
    public void publishMessage(final String content, final String user) {
        model.publish(content, user);
        for (String userId : model.getUserIds()) {
            view.updateMessageListForUser(model.getSortedMessages(userId), userId);
        }
        refreshMessages();
    }

    /** Calls the model's method to delete a message. */
    public void deleteMessage(final MessageDecorator message, final String userId) {
        if (message.getUserId().equals(userId)) {
            model.removeMessage(message);
            for (String user : model.getUserIds()) {
                view.updateMessageListForUser(model.getSortedMessages(user), user);
            }
            refreshMessages();
        }
    }

    /** Calls the model's method to bookmark the message. */
    public void toggleBookmark(final MessageDecorator message, final String userId) {
        model.bookmarkMessage(message, userId);
        view.updateMessageListForUser(model.getSortedMessages(userId), userId);
    }

    /** Getter for bookmark. */
    public boolean isMessageBookmarked(final MessageDecorator message, final String userId) {
        return model.isMessageBookmarked(message, userId);
    }

    /** Getter for the score. */
    public int getMessageScore(final MessageDecorator message, final String userId) {
        return model.getMessageScore(message, userId);
    }

    /** Search function updater. */
    public void searchMessages(final String search, final String userId) {
        List<MessageDecorator> filteredMessages = model.getFilteredMessages(search, userId);
        view.updateMessageListForUser(filteredMessages, userId);
    }

    /** Calls the model to make sure that the message should be displayed. */
    public boolean shouldDisplay(final MessageDecorator message,
                                 final String userId, final int scoreThreshold) {
        return model.shouldDisplay(message, userId, scoreThreshold);
    }
}
