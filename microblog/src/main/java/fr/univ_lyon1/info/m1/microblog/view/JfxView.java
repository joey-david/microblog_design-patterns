package fr.univ_lyon1.info.m1.microblog.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import fr.univ_lyon1.info.m1.microblog.controller.Controller;
import fr.univ_lyon1.info.m1.microblog.model.BookmarkScoring;
import fr.univ_lyon1.info.m1.microblog.model.Message;
import fr.univ_lyon1.info.m1.microblog.model.MessageData;
import fr.univ_lyon1.info.m1.microblog.model.User;
import fr.univ_lyon1.info.m1.microblog.model.Y;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.Text;

/**
 * Main class of the View (GUI) of the application.
 */
public class JfxView {
    private Controller controller;
    private HBox users;
    private VBox messageList;

    /**
     * Main view of the application.
     */
        // TODO: style error in the following line. Check that checkstyle finds it, and then fix it.
        public JfxView(final Stage stage,
                       final int width, final int height) {
        stage.setTitle("Y Microblogging");

        final VBox root = new VBox(10);

        // final Pane search = createSearchWidget();
        // root.getChildren().add(search);

        users = new HBox(10);
        messageList = new VBox(10);
        root.getChildren().addAll(users, messageList);

        // Everything's ready: add it to the scene and display it
        final Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public void setController(Controller controller) {
            this.controller = controller;
    }

    public void updateUserList(Collection<User> userList) {
            users.getChildren().clear();
            for(User u: userList) {
                Button userButton = new Button(u.getId());
                userButton.setOnAction(e -> createMessageInputForUser(u));
                users.getChildren().add(userButton);
            }
    }

    public void updateMessageList(List<Message> messages) {
        messageList.getChildren().clear();
        for(Message m: messages) {
            Label messageLabel = new Label(/*m.getUserId() + ":" +*/ m.getContent());
            messageList.getChildren().add(messageLabel);
        }
    }

    private void createMessageInputForUser(User user) {
            Stage stage = new Stage();
            VBox input = new VBox(10);
            TextArea messageInput = new TextArea();
            Button sendButton = new Button("Send");
            sendButton.setOnAction(e -> {
               controller.publishMessage(messageInput.getText());
               stage.close();
            });
            input.getChildren().addAll(messageInput, sendButton);
            stage.setScene(new Scene(input, 300, 200));
            stage.show();
    }

}
