"use strict";

/**
 * Conference service to interact with the Conference API
 */
feedbackApp.service("CacheService", function () {

    var conferences = undefined;
    var sessions = [];

    return {
        setConferences : function(conferencesFetch) {
            conferences = conferencesFetch;
        },
        getConferences: function() {
            return conferences;
        },
        setSessions : function(idConf, sessionsFetch) {
            sessions[idConf] = sessionsFetch;
        },
        getSessions: function(idConf) {
            return sessions[idConf];
        }
    };
});