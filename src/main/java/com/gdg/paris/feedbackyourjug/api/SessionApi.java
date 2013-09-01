package com.gdg.paris.feedbackyourjug.api;

import com.gdg.paris.feedbackyourjug.dao.GenericDao;
import com.gdg.paris.feedbackyourjug.model.Comment;
import com.gdg.paris.feedbackyourjug.model.Session;
import com.gdg.paris.feedbackyourjug.model.dto.CommentDto;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import javax.inject.Named;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API to manage the conferences.
 * (new, list, delete...)
 */
@Api(
        name = "feedbackyourjug",
        version = "v1",
        clientIds = {Ids.WEB_CLIENT_ID, Ids.API_EXPLORER_ID}
)
public class SessionApi {

    private final GenericDao<Session> sessionDao = new GenericDao<>(Session.class);

    /**
     * Get the list of Sessions for a done conference.
     *
     * @param idConference the id of the conference
     * @return the list of the Sessions
     */
    @ApiMethod(
            name = "sessions.list",
            path = "sessions/{idConference}",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public List<Session> getSessions(@Named("idConference") String idConference) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("idConference", Long.valueOf(idConference));
        return sessionDao.getEntities(searchParams);
    }

    /**
     * Get the session with the id
     *
     * @param id the id of the session
     * @return the session
     */
    @ApiMethod(
            name = "sessions.get",
            path = "session/{id}",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public Session getSession(@Named("id") String id) {
        return sessionDao.getEntityById(Long.valueOf(id));
    }

    @ApiMethod(
            name = "sessions.update.comment",
            path = "session/comment",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public Session addComment(User user, CommentDto newComment) throws OAuthRequestException,
            IOException {
        if (user == null) {
            throw new OAuthRequestException("Invalid user.");
        }
        Session sessionToUpdate = sessionDao.getEntityById(Long.valueOf(newComment.getSessionId()));
        if (sessionToUpdate != null) {
            Comment commentaireToAdd = newComment.getComment();
            commentaireToAdd.setAuthorNickname(newComment.getNickName());
            commentaireToAdd.setAuthorEmail(user.getEmail());
            sessionToUpdate.getComments().add(commentaireToAdd);
            sessionDao.insertEntity(sessionToUpdate);
        }
        return sessionToUpdate;
    }

}
