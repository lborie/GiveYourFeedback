package com.gdg.paris.feedbackyourjug.model;

import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

@Embed
public class Comment {

    @Id
    private Long id;

    private Date creationDate;

    private String authorNickname;

    private String authorEmail;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAuthorNickname() {
        return authorNickname;
    }

    public void setAuthorNickname(String authorNickname) {
        this.authorNickname = authorNickname;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
