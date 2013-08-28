package com.gdg.paris.feedbackyourjug.model.dto;

import com.gdg.paris.feedbackyourjug.model.Comment;
import com.googlecode.objectify.annotation.Embed;

import java.util.Date;

public class CommentDto {

    private Comment comment;

    private String sessionId;

    private String nickName;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
