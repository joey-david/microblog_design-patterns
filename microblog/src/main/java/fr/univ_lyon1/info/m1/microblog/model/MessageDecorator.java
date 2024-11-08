package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Date;

/** Decorator for Message that includes MessageData. */
public class MessageDecorator extends Message {
    private MessageData data;

    /** Default constructor. */
    public MessageDecorator(String content) {
        super(content);
        data = new MessageData();
    }


    /** Get the data associated with the message. */
    public MessageData getData() {
        return data;
    }

    /** Set the data associated with the message. */
    public void setData(MessageData data) {
        this.data = data;
    }

    /** Get the score of the message. */
    public int getScore() {
        return data.getScore();
    }

    /** Set the score of the message. */
    public void setScore(int score) {
        data.setScore(score);
    }

    /** Check if the message is bookmarked. */
    public boolean isBookmarked() {
        return data.isBookmarked();
    }

    /** Set the bookmarked status of the message. */
    public void setBookmarked(boolean bookmarked) {
        data.setBookmarked(bookmarked);
    }
}
