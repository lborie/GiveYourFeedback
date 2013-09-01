"use strict";

/**
 * Conference service to interact with the Conference API
 */
feedbackApp.service("ConferenceService", function ($http) {
    var CURRENT_ROOT = ROOT_API + '/conference';

    return {
        fetch : function() {
            return $http.get(CURRENT_ROOT);
        },
        insertConference: function(conference) {
            return gapi.client.feedbackyourjug.conferences.insert(conference);
        }
    };
});