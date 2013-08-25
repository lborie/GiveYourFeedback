package com.gdg.paris.feedbackyourjug.api;

import com.gdg.paris.feedbackyourjug.dao.GenericDao;
import com.gdg.paris.feedbackyourjug.model.Session;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;

import javax.inject.Named;
import java.util.ArrayList;
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
        clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID, Ids.IOS_CLIENT_ID, Ids.API_EXPLORER_ID},
        audiences = {Ids.ANDROID_AUDIENCE}
)
public class SessionApi {

    private final GenericDao<Session> sessionDao = new GenericDao<>(Session.class);

    /**
     * Get the list of Sessions for a done conference.
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
        List<Session> sessions = sessionDao.getEntities(searchParams);

        if (sessions == null || sessions.isEmpty()) {
            sessions = new ArrayList<>();
            Session session = new Session();
            session.setId(1L);
            session.setIdConference(1L);
            session.setTitle("Keynote");
            session.setSpeaker("David Gageot");
            session.setDescription("Keanote d'introduction");
            sessions.add(session);
        }
        return sessions;
    }

    /**
     * Get the session with the id
     * @param id the id of the session
     * @return the session
     */
    @ApiMethod(
            name = "sessions.get",
            path = "session/{id}",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public Session getSession(@Named("id") String id) {
        Session session = sessionDao.getEntityById(Long.valueOf(id));

        if (session == null) {
            session = new Session();
            session.setId(1L);
            session.setTitle("Keynote");
            session.setSpeaker("David Gageot");
            session.setDescription("Keanote d'introduction");
        }
        return session;
    }

    @ApiMethod(
            name = "sessions.update",
            path = "session",
            httpMethod = ApiMethod.HttpMethod.PUT
    )
    public Session updateSession(Session session, User user) {
        sessionDao.insertEntity(session);
        return session;
    }

}
