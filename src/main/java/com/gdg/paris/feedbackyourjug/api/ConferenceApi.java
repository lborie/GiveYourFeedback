package com.gdg.paris.feedbackyourjug.api;

import com.gdg.paris.feedbackyourjug.dao.GenericDao;
import com.gdg.paris.feedbackyourjug.model.Conference;
import com.gdg.paris.feedbackyourjug.model.Session;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

import java.util.List;

/**
 * API to manage the conferences.
 * (new, list, delete...)
 */
@Api(
        name = "feedbackyourjug",
        version = "v1",
        clientIds = {Ids.WEB_CLIENT_ID, Ids.API_EXPLORER_ID}
)
public class ConferenceApi {

    private final GenericDao<Conference> conferenceDao = new GenericDao<>(Conference.class);

    @ApiMethod(
            name = "conferences.list",
            path = "conference",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public List<Conference> getConferences() {
        return conferenceDao.getEntities();
    }

    @ApiMethod(
            name = "conferences.insert",
            path = "conference",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public Conference insertConference(Conference conference) {
        conferenceDao.insertEntity(conference);
        return conference;
    }

    @ApiMethod(
            name = "conferences.update",
            path = "conference",
            httpMethod = ApiMethod.HttpMethod.PUT
    )
    public Conference updateConference(Conference conference) {
        conferenceDao.insertEntity(conference);
        return conference;
    }

    @ApiMethod(
            name = "conferences.fake",
            path = "conference/fake",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public Conference fakeConference() {
        Conference jugSummerCamp = new Conference();
        jugSummerCamp.setLocation("La Rochelle");
        jugSummerCamp.setName("Jug Summer Camp");
        jugSummerCamp.setId(1L);
        conferenceDao.insertEntity(jugSummerCamp);

        GenericDao<Session> sessionDao = new GenericDao<>(Session.class);
        Session session = new Session();
        session.setId(1L);
        session.setIdConference(1L);
        session.setTitle("Keynote");
        session.setSpeaker("David Gageot");
        session.setDescription("Keanote d'introduction");
        sessionDao.insertEntity(session);

        return jugSummerCamp;
    }

}
