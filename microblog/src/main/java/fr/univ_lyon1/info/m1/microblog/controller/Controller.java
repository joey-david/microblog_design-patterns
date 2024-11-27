package fr.univ_lyon1.info.m1.microblog.controller;

import fr.univ_lyon1.info.m1.microblog.view.JfxView;
import fr.univ_lyon1.info.m1.microblog.model.MostRelevantScoring;
import fr.univ_lyon1.info.m1.microblog.model.ScoringStrategy;
import fr.univ_lyon1.info.m1.microblog.model.ChronologicalScoring;
import fr.univ_lyon1.info.m1.microblog.model.MessageDecorator;
import fr.univ_lyon1.info.m1.microblog.model.Y;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.stream.Collectors;

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
                view.updateUserList(model.getUsers());
                break;
            case "MESSAGE_ADDED":
            case "SCORING_STRATEGY_CHANGED":
            case "MESSAGE_BOOKMARKED":
            case "MESSAGE_REMOVED":
                view.updateMessageList(model.getSortedMessages());
                refreshMessages();
                break;
            default:
                break;
        }
    }

    private void refreshMessages() {
        searchMessages(currentSearchQuery);
    }

    /** Calls the model's method to switch the scoring strategy. */
    public void switchScoringStrategy(final ScoringStrategy strategy) {
        model.setScoringStrategy(strategy);
        strategy.computeScores(model.getMessages());
        if (strategy instanceof ChronologicalScoring) {
            view.setScoreThreshold(-1);
        } else if (strategy instanceof MostRelevantScoring) {
            view.setScoreThreshold(0);
        }
        view.updateMessageList(model.getSortedMessages());
        refreshMessages();
    }

    /** Calls the model's method to create the user. */
    public void createUser(final String id) {
        model.createUser(id);
    }

    /** Calls the model's method to publish a message. */
    public void publishMessage(final String content) {
        model.add(content);
        view.updateMessageList(model.getSortedMessages());
        refreshMessages();
    }

    /** Calls the model's method to delete a message. */
    public void deleteMessage(final MessageDecorator message) {
        model.removeMessage(message);
        view.updateMessageList(model.getSortedMessages());
        refreshMessages();
    }

    /** Calls the model's method to bookmark the message. */
    public void toggleBookmark(final MessageDecorator message) {
        model.bookmarkMessage(message);
        view.updateMessageList(model.getSortedMessages());
    }

    /** Getter for bookmark. */
    public boolean isMessageBookmarked(final MessageDecorator message) {
        return model.isMessageBookmarked(message);
    }

    /** Getter for the score. */
    public int getMessageScore(final MessageDecorator message) {
        return model.getMessageScore(message);
    }

    /** Search function updater. */
    public void searchMessages(final String search) {
        currentSearchQuery = search;
        List<MessageDecorator> filteredMessages = model.getMessages().stream()
                .filter(message -> isValidLookup(message.getContent(), search))
                .collect(Collectors.toList());
        view.updateMessageList(filteredMessages);
    }

    /** search boolean function. */
    private boolean isValidLookup(final String message, final String query) {
        return message.toLowerCase().contains(query.toLowerCase());
    }
}