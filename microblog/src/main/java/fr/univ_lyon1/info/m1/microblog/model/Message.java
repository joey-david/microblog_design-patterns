package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Date;

/**
 * Message and its own data.
 */
public class Message {
    private String content;
    private Date publicationDate;

    public String getContent() {
        return content;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }


    /**
     * Build a Message object from it's (String) content.
     */
    public Message(final String content) {
        this.content = content;
        this.publicationDate = new Date();
    }

}
