package fr.univ_lyon1.info.m1.microblog.model;
import java.util.Date;

/** Decorator for Message that includes MessageData. */
public class MessageDecorator extends Message {
    private MessageData data;

    /** Default constructor with content. */
    public MessageDecorator(final String content) {
        super(content);
        data = new MessageData();
    }

    /** Csontructor with content and userId. */
    public MessageDecorator(final String content, final String userId) {
        super(content, userId);
        data = new MessageData();
    }

    /** Get the data associated with the message. */
    public MessageData getData() {
        return data;
    }

    /** Set the data associated with the message. */
    public void setData(final MessageData data) {
        this.data = data;
    }

    /** Get the publication date of the message. */
    public Date getPublicationDate() {
        return super.getPublicationDate();
    }
}
