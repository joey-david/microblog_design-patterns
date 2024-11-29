package fr.univ_lyon1.info.m1.microblog.model;

import java.util.Date;
import java.util.UUID;

/**
 * Message and its own data.
 */
public class Message { //TODO: add string UserId and adapt bookmarks so it only bookmarks for the specific user, same for deleting only for the creator
    private String content;
    private Date publicationDate;
    private String userId;
    private final UUID messageId;

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

    public UUID getMessageId() {
        return messageId;
    }


    /**
     * Build a Message object from it's (String) content.
     */
    public Message(final String content) {
        this.content = content;
        this.publicationDate = new Date();
        this.messageId = UUID.randomUUID();
    }

    public Message(final String content, final String userId) {
        this.content = content;
        this.userId = userId;
        this.publicationDate = new Date();
        this.messageId = UUID.randomUUID();
    }
}
