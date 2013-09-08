"use strict";

/**
 * Conference service to interact with the Conference API
 */
feedbackApp.service("UserService", function () {

    var currentUser = undefined;

    var currentToken = undefined;

    return {
        setUser : function(user) {
            currentUser = user;
        },
        setToken: function(token) {
            currentToken = token;
        },
        getUser : function() {
            return currentUser;
        },
        getToken : function() {
            return currentToken;
        },
        isLogged: function() {
            return currentUser;
        }
    };
});