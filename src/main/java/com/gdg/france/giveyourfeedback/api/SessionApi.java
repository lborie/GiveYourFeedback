package com.gdg.france.giveyourfeedback.api;

import com.gdg.france.giveyourfeedback.dao.GenericDao;
import com.gdg.france.giveyourfeedback.model.Comment;
import com.gdg.france.giveyourfeedback.model.Session;
import com.gdg.france.giveyourfeedback.model.dto.CommentDto;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import javax.inject.Named;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API to manage the conferences.
 * (new, list, delete...)
 */
@Api(
        name = "giveyourfeedback",
        version = "v2",
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
    public Session newComment(User user, CommentDto newComment) throws OAuthRequestException,
            IOException {
        Session sessionToUpdate = sessionDao.getEntityById(Long.valueOf(newComment.getSessionId()));
        if (sessionToUpdate != null) {
            Comment commentaireToAdd = newComment.getComment();
            commentaireToAdd.setAuthorNickname(newComment.getNickName());
            if (user != null) {
                commentaireToAdd.setAuthorEmail(user.getEmail());
            }
            commentaireToAdd.setCreationDate(new Date());
            sessionToUpdate.getComments().add(commentaireToAdd);
            sessionDao.insertEntity(sessionToUpdate);
        }
        return sessionToUpdate;
    }

}
