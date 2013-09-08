package com.gdg.paris.feedbackyourjug.api;

import com.gdg.paris.feedbackyourjug.dao.GenericDao;
import com.gdg.paris.feedbackyourjug.model.Conference;
import com.gdg.paris.feedbackyourjug.model.Session;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
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
    public Conference insertConference(Conference conference, User user) throws OAuthRequestException,
            IOException, ServiceException {
        if (user == null) {
            throw new OAuthRequestException("Invalid user.");
        }

        SpreadsheetService service = new SpreadsheetService("give-your-feedback");
        service.setAuthSubToken(conference.getUserToken());
        service.useSsl();

        SpreadsheetEntry entry = service.getEntry(new URL("https://spreadsheets.google.com/feeds/spreadsheets/" + conference.getSpreadSheetId()), SpreadsheetEntry.class);
        WorksheetEntry wsEntry = entry.getDefaultWorksheet();

        ListFeed listFeed = service.getFeed(wsEntry.getListFeedUrl(), ListFeed.class);
        for (ListEntry row : listFeed.getEntries()) {
            for (String tag : row.getCustomElements().getTags()) {
                System.out.print(tag + " : " + row.getCustomElements().getValue(tag) + "\t");
            }
        }

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
