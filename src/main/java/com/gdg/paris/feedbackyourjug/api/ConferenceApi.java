package com.gdg.paris.feedbackyourjug.api;

import com.gdg.paris.feedbackyourjug.dao.GenericDao;
import com.gdg.paris.feedbackyourjug.model.Conference;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * API to manage the conferences.
 * (new, list, delete...)
 */
@Api(
        name = "feedbackyourjug",
        version = "v1",
        clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID, Ids.IOS_CLIENT_ID},
        audiences = {Ids.ANDROID_AUDIENCE}
)
public class ConferenceApi {

    private final GenericDao<Conference> conferenceDao = new GenericDao<>(Conference.class);

    @ApiMethod(
            name = "conferences.list",
            path = "conference",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public List<Conference> getConferences() {
        List<Conference> conferences = conferenceDao.getEntities();
        if (conferences == null || conferences.isEmpty()) {
            conferences = new ArrayList<>();
            Conference jugSummerCamp = new Conference();
            jugSummerCamp.setLocation("La Rochelle");
            jugSummerCamp.setName("Jug Summer Camp");
            conferences.add(jugSummerCamp);
        }
        return conferences;
    }

    @ApiMethod(
            name = "conferences.insert",
            path = "conference",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public Conference insertConference(Conference conference){
        conferenceDao.insertEntity(conference);
        return conference;
    }

    @ApiMethod(
            name = "conferences.update",
            path = "conference",
            httpMethod = ApiMethod.HttpMethod.PUT
    )
    public Conference updateConference(Conference conference){
        conferenceDao.insertEntity(conference);
        return conference;
    }

}
