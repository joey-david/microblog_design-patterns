package fr.univ_lyon1.info.m1.microblog.view;

import fr.univ_lyon1.info.m1.microblog.controller.Controller;

import fr.univ_lyon1.info.m1.microblog.model.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

/**
 * Main class of the View (GUI) of the application.
 */
public class JfxView {
    private Controller controller;
    private HBox users;
    private VBox root;
    private int scoreThreshold = 0;

    /**
     * Main view of the application.
     */
    public JfxView(final Stage stage,
                   final int width, final int height) {
        stage.setTitle("Y Microblogging");

        root = new VBox(10);

        users = new HBox(10);
        root.getChildren().add(users);

        // Everything's ready: add it to the scene and display it
        final Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Set the controller for the view.
     */
    public void setController(final Controller controller) {
        this.controller = controller;
    }

    /**
     * Create the pane containing all user's, from the users' registry passed as an argument.
     */
    public void updateUserList(final Collection<String> userList) {
        users.getChildren().clear();
        for (String s : userList) {
            ScrollPane p = new ScrollPane();
            VBox userBox = new VBox();
            p.setMinWidth(300);
            p.setContent(userBox);
            users.getChildren().add(p);

            VBox userMsgBox = new VBox();

            Label userID = new Label(s);

            TextField searchBar = new TextField();
            searchBar.setPromptText("Search messages");
            searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.searchMessages(newValue, s);
            });

            ComboBox<ScoringStrategy> strategyComboBox =
                    new ComboBox<>(FXCollections.observableArrayList(
                        new ChronologicalScoring(),
                        new RecentRelevantScoring(),
                        new MostRelevantScoring()
            ));
            strategyComboBox.setOnAction(e -> controller.switchScoringStrategy(
                    strategyComboBox.getValue(), s));

            // Use toString() method for displaying items
            strategyComboBox.setCellFactory(lv -> new ListCell<ScoringStrategy>() {
                @Override
                protected void updateItem(final ScoringStrategy item, final boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.toString());
                }
            });

            strategyComboBox.setButtonCell(strategyComboBox.getCellFactory().call(null));
            strategyComboBox.getSelectionModel().select(1); // Select MostRelevantScoring by default

            HBox strategyBox = new HBox(10, strategyComboBox, searchBar);
            strategyBox.setAlignment(Pos.CENTER);

            Pane textBox = createInputWidget(s);
            userBox.getChildren().addAll(userID, strategyBox, userMsgBox, textBox);
        }
    }

    /**
     * Create the pane containing all messages, from the messages' registry passed as an argument.
     */
    public void updateMessageListForUser(final List<MessageDecorator> messages,
                                         final String userId) {
        users.getChildren().stream()
                .filter(node -> node instanceof ScrollPane)
                .map(node -> (ScrollPane) node)
                .forEach(scrollPane -> {
                    VBox userBox = (VBox) scrollPane.getContent();
                    Label userIdLabel = (Label) userBox.getChildren().get(0);
                    String user = userIdLabel.getText();
                    if (user.equals(userId)) {
                        VBox userMsgBox = (VBox) userBox.getChildren().get(2);
                        userMsgBox.getChildren().clear();

                        for (MessageDecorator message : messages) {
                            if (message.getScore() > scoreThreshold) {
                                VBox msgBox = createMessageWidget(message, userId);
                                userMsgBox.getChildren().add(msgBox);
                            }
                        }
                    }
                });
    }

    static final String MSG_STYLE = "-fx-background-color: white; "
            + "-fx-border-color: black; -fx-border-width: 1;"
            + "-fx-border-radius: 10px;"
            + "-fx-background-radius: 10px;"
            + "-fx-padding: 8px; "
            + "-fx-margin: 5px; ";

    private VBox createMessageWidget(final MessageDecorator m, final String userId) {
        VBox msgBox = new VBox();
        String bookmarkText = controller.isMessageBookmarked(m, userId) ? "⭐" : "Click to bookmark";

        // Buttons are stored in the buttonBox
        HBox buttonBox = new HBox();

        // bookmark button
        Button bookButton = new Button(bookmarkText);
        bookButton.setOnAction(e -> {
            controller.toggleBookmark(m, userId);
        });
        buttonBox.getChildren().add(bookButton);

        // delete button, only if the user is the author
        if (m.getUserId().equals(userId)) {
            Button deleteButton = new Button("❌");
            deleteButton.setOnAction(e -> {
                controller.deleteMessage(m, userId);
            });
            buttonBox.getChildren().add(deleteButton);
        }

        msgBox.getChildren().add(buttonBox);

        final Label label = new Label(m.getContent());
        msgBox.getChildren().add(label);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Label date = new Label(dateFormat.format(m.getPublicationDate()));
        msgBox.getChildren().add(date);

        final Label score = new Label("Score: " + m.getScore());
        score.setTextFill(Color.LIGHTGRAY);

        final Label author = new Label("Author: " + controller.getUsernameById(m.getUserId()));
        author.setTextFill(Color.GRAY);

        HBox scoreAuthorBox = new HBox(10, score, author);
        HBox.setHgrow(author, Priority.ALWAYS);
        author.setMaxWidth(Double.MAX_VALUE);
        scoreAuthorBox.setAlignment(Pos.CENTER_LEFT);
        msgBox.getChildren().add(scoreAuthorBox);

        msgBox.setStyle(MSG_STYLE);
        return msgBox;
    }

    private Pane createInputWidget(final String u) {
        final Pane input = new HBox();
        TextArea t = new TextArea();
        t.setMaxSize(200, 150);
        t.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && e.isControlDown()) {
                controller.publishMessage(t.getText(), u);
                t.clear();
            }
        });
        Button s = new Button("Publish");
        s.setOnAction(e -> {
            controller.publishMessage(t.getText(), u);
            t.clear();
        });
        input.getChildren().addAll(t, s);
        return input;
    }

    public void setScoreThreshold(final int i) {
        scoreThreshold = i;
    }
}
