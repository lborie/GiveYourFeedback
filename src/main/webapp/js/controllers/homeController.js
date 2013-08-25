"use strict";

/**
 * Controller of the home page
 */
feedbackApp.controller("homeController" ,function ($scope) {

    $scope.project = 'Start Endpoints';
    $scope.loginAction = 'Login';

    $scope.signin = function(mode, callback) {
        gapi.auth.authorize({
            client_id: '700903555117.apps.googleusercontent.com',
            scope: 'https://www.googleapis.com/auth/userinfo.profile',
            immediate: mode,
            response_type: 'token id_token'},
            callback
        );
    }

    $scope.userAuthed = function() {
        console.log('tata');
        var request = gapi.client.oauth2.userinfo.get().execute(function(resp) {
            console.log(resp);
            if (!resp.code) {
                $scope.loginAction = resp.name;
                $scope.$apply();
                var token = gapi.auth.getToken();
                token.access_token = token.id_token;
                gapi.auth.setToken(token);
                // User is signed in, call my Endpoint
            }
        });
    }
    $scope.auth = function() {
        $scope.signin(false, $scope.userAuthed);
    };

});
