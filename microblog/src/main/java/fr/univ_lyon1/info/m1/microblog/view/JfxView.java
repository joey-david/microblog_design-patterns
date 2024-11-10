package fr.univ_lyon1.info.m1.microblog.view;

import fr.univ_lyon1.info.m1.microblog.controller.Controller;
import fr.univ_lyon1.info.m1.microblog.model.MessageDecorator;
import fr.univ_lyon1.info.m1.microblog.model.User;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    /**
     * Main view of the application.
     */
    // TODO: style error in the following line. Check that checkstyle finds it, and then fix it.
    public JfxView(final Stage stage,
                   final int width, final int height) {
        stage.setTitle("Y Microblogging");

        root = new VBox(10);

        // final Pane search = createSearchWidget();
        // root.getChildren().add(search);

        users = new HBox(10);
        root.getChildren().add(users);

        // Everything's ready: add it to the scene and display it
        final Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void setController(final Controller controller) {
        this.controller = controller;
    }

    /**
     * Create the pane containing all user's, from the users' registry passed as an argument.
     */
    public void updateUserList(final Collection<User> userList) {
        users.getChildren().clear();
        for (User u : userList) {
            ScrollPane p = new ScrollPane();
            VBox userBox = new VBox();
            p.setMinWidth(300);
            p.setContent(userBox);
            users.getChildren().add(p);

            VBox userMsgBox = new VBox();

            Label userID = new Label(u.getId());

            Pane textBox = createInputWidget(u);
            userBox.getChildren().addAll(userID, userMsgBox, textBox);
        }
    }

    /**
     *
     */
    public void updateMessageList(final List<MessageDecorator> messages) {
        users.getChildren().stream()
                .filter(node -> node instanceof ScrollPane)
                .map(node -> (ScrollPane) node)
                .forEach(scrollPane -> {
                    VBox userBox = (VBox) scrollPane.getContent();
                    VBox userMsgBox = (VBox) userBox.getChildren().get(1);
                    userMsgBox.getChildren().clear();

                    for (MessageDecorator message : messages) {
                        VBox msgBox = createMessageWidget(message);
                        userMsgBox.getChildren().add(msgBox);
                    }
                });
    }


    static final String MSG_STYLE = "-fx-background-color: white; "
            + "-fx-border-color: black; -fx-border-width: 1;"
            + "-fx-border-radius: 10px;"
            + "-fx-background-radius: 10px;"
            + "-fx-padding: 8px; "
            + "-fx-margin: 5px; ";

    private VBox createMessageWidget(final MessageDecorator m) {
        VBox msgBox = new VBox();
        String bookmarkText = m.isBookmarked() ? "⭐" : "Click to bookmark";

        // Buttons are stored in the buttonBox
        HBox buttonBox = new HBox();

        // bookmark button
        Button bookButton = new Button(bookmarkText);
        bookButton.setOnAction(e -> {
            controller.toggleBookmark(m);
        });
        // delete button
        Button deleteButton = new Button("❌");
        deleteButton.setOnAction(e -> {
            controller.deleteMessage(m);
        });

        buttonBox.getChildren().addAll(bookButton, deleteButton);
        msgBox.getChildren().add(buttonBox);

        final Label label = new Label(m.getContent());
        msgBox.getChildren().add(label);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Label date = new Label(dateFormat.format(m.getPublicationDate()));
        msgBox.getChildren().add(date);

        final Label score = new Label("Score: " + m.getScore());
        score.setTextFill(Color.LIGHTGRAY);
        msgBox.getChildren().add(score);

        msgBox.setStyle(MSG_STYLE);
        return msgBox;
    }

    private Pane createInputWidget(final User u) {
        final Pane input = new HBox();
        TextArea t = new TextArea();
        t.setMaxSize(200, 150);
        t.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && e.isControlDown()) {
                controller.publishMessage(t.getText());
                t.clear();
            }
        });
        Button s = new Button("Publish");
        s.setOnAction(e -> {
            controller.publishMessage(t.getText());
            t.clear();
        });
        input.getChildren().addAll(t, s);
        return input;
    }
}
