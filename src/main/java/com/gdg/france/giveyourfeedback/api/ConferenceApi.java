package com.gdg.france.giveyourfeedback.api;

import com.gdg.france.giveyourfeedback.dao.GenericDao;
import com.gdg.france.giveyourfeedback.model.Conference;
import com.gdg.france.giveyourfeedback.model.Session;
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
        name = "giveyourfeedback",
        version = "v2",
        clientIds = {Ids.WEB_CLIENT_ID, Ids.API_EXPLORER_ID}
)
public class ConferenceApi {

    private final GenericDao<Conference> conferenceDao = new GenericDao<>(Conference.class);
    private final GenericDao<Session> sessionDao = new GenericDao<>(Session.class);
    private final static Logger logger = Logger.getLogger(ConferenceApi.class.getName());

    private final static String ADMINISRATOR = "no-reply@give-your-feedback.appspotmail.com";

    @ApiMethod(
            name = "conferences.list",
            path = "conference",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public List<Conference> getConferences() {
        List<Conference> allConferences = conferenceDao.getEntities();
        Predicate<Conference> validatedConference = new Predicate<Conference>() {
            @Override
            public boolean apply(@Nullable Conference conference) {
                return Boolean.TRUE.equals(conference.isValidated());
            }
        };
        return Lists.newArrayList(Iterables.filter(allConferences, validatedConference));
//        return conferenceDao.getEntities();
    }

    @ApiMethod(
            name = "conferences.insert",
            path = "conference",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public Conference insertConference(Conference conference, User user) throws OAuthRequestException,
            IOException, ServiceException, MessagingException {
        if (user == null) {
            throw new OAuthRequestException("Invalid user.");
        }

        // Owner
        conference.setOwnerUserEmail(user.getEmail());
        conference.setValidated(false);
        conferenceDao.insertEntity(conference);

        // Sessions
        SpreadsheetService service = new SpreadsheetService("give-your-feedback");
        service.setAuthSubToken(conference.getUserToken());
        service.useSsl();

        URL spreadsheetUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/" + conference.getSpreadSheetId());
        SpreadsheetEntry entry = service.getEntry(spreadsheetUrl, SpreadsheetEntry.class);
        WorksheetEntry wsEntry = entry.getDefaultWorksheet();

        List<Session> sessions = new ArrayList<>();

        ListFeed listFeed = service.getFeed(wsEntry.getListFeedUrl(), ListFeed.class);
        for (ListEntry row : listFeed.getEntries()) {
            Session session = new Session();
            session.setIdConference(conference.getId());
            sessions.add(session);
            for (String tag : row.getCustomElements().getTags()) {
                switch (tag) {
                    case "titre":
                        session.setTitle(row.getCustomElements().getValue(tag));
                        break;
                    case "speakers":
                        session.setSpeaker(row.getCustomElements().getValue(tag));
                        break;
                    case "type":
                        session.setType(row.getCustomElements().getValue(tag));
                        break;
                    case "salle":
                        session.setLocation(row.getCustomElements().getValue(tag));
                        break;
                    case "début":
                        session.setStartTime(row.getCustomElements().getValue(tag));
                        break;
                    case "fin":
                        session.setEndTime(row.getCustomElements().getValue(tag));
                        break;
                    case "date":
                        session.setDay(row.getCustomElements().getValue(tag));
                        break;
                    case "description":
                        session.setDescription(row.getCustomElements().getValue(tag));
                        break;
                }
                System.out.print(tag + " : " + row.getCustomElements().getValue(tag) + "\t");
            }
        }
        sessionDao.insertEntities(sessions);

        sendMail(conference);

        return conference;
    }

    private void sendMail(Conference conference) throws UnsupportedEncodingException, MessagingException {
        Properties props = new Properties();
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);

        try {
            String encodingOptions = "text/html; charset=UTF-8";
            MimeMessage msg = new MimeMessage(session);
            msg.setHeader("Content-Type", encodingOptions);
            msg.setFrom(new javax.mail.internet.InternetAddress(ADMINISRATOR, "Give Your Feedback"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("admins"));
            msg.setSubject("Nouvelle conférence créée !", "UTF-8");
            msg.setText("Une nouvelle conférence vient d'être créée (" + conference.getName() + "), et est en attente de votre validation.");

            Transport.send(msg);

        } catch (UnsupportedEncodingException | MessagingException e) {
            logger.log(Level.SEVERE, "Mail send error", e);
            throw e;
        }
    }
}
