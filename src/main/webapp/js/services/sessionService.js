"use strict";

/**
 * Conference service to interact with the Conference API
 */
feedbackApp.service("SessionService", function ($http) {
    var CURRENT_ROOT = ROOT_API;

    return {
        getSessions : function(idConference) {
            return $http.get(CURRENT_ROOT + '/sessions/' + idConference);
        },
        getSession : function(id) {
            return $http.get(CURRENT_ROOT + '/session/' + id);
        },
        newComment: function(commentDto) {
            return gapi.client.feedbackyourjug.sessions.update.comment(commentDto);
        }
    };
});