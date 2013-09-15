package com.gdg.paris.feedbackyourjug.api;

import com.gdg.paris.feedbackyourjug.dao.GenericDao;
import com.gdg.paris.feedbackyourjug.model.Conference;
import com.gdg.paris.feedbackyourjug.model.Session;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.common.base.Nullable;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * API to manage the conferences.
 * (new, list, delete...)
 */
@Api(
        name = "feedbackyourjug",
        version = "v2",
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
    public Conference insertConference(Conference conference) throws OAuthRequestException,
            IOException, ServiceException, MessagingException {
        conferenceDao.insertEntity(conference);
        return conference;
    }

}
