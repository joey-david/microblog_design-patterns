package fr.univ_lyon1.info.m1.microblog.model;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/** Interface for the Message factories. */
public interface MessageFactory {
    /** Create Message from content. */
    MessageDecorator createMessage(String content);
    /** Create Message from content and user. */
    MessageDecorator createMessage(String content, String userId);
    /** Create Message from content and publication date. */
    MessageDecorator createMessage(String content, Date publicationDate);
    /** Load messages from a file. */
    List<MessageDecorator> loadMessagesFromFile(String resourcePath) throws IOException;
}
