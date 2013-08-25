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
        save: function(conference) {
            return $http.post(CURRENT_ROOT, conference);
        }
    };
});