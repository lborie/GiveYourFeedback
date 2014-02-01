package com.gdg.france.giveyourfeedback.model.dto;

import com.gdg.france.giveyourfeedback.model.Comment;

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
