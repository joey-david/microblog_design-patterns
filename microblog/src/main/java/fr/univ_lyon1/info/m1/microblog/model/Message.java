package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Date;

/**
 * Message and its own data.
 */
public class Message { //TODO: add string UserId and adapt bookmarks so it only bookmarks for the specific user, same for deleting only for the creator
    private String content;
    private Date publicationDate;
    private String userId;

    public String getContent() {
        return content;
    }

    public String getUserId() { return userId; }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(final Date publicationDate) {
        this.publicationDate = publicationDate;
    }


    /**
     * Build a Message object from it's (String) content.
     */
    public Message(final String content) {
        this.content = content;
        this.publicationDate = new Date();
    }

    public Message(final String content, final String userId) {
        this.content = content;
        this.userId = userId;
        this.publicationDate = new Date();
    }
}
