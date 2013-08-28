"use strict";

/**
 * Controller of the home page
 */
feedbackApp.controller("homeController" ,function ($scope, $window) {

    $scope.project = 'Start Endpoints';
    $scope.loginAction = 'Login';
    $scope.logged = false;

    $scope.signin = function(mode, callback) {
        if (!$scope.logged) {
            gapi.auth.authorize({
                    client_id: '700903555117.apps.googleusercontent.com',
                    scope: 'https://www.googleapis.com/auth/userinfo.profile',
                    immediate: mode,
                    response_type: 'token id_token'},
                callback
            );
        }
    }

    $scope.userAuthed = function() {
        gapi.client.oauth2.userinfo.get().execute(function(resp) {
            console.log(resp);
            if (!resp.code) {
                $scope.logged = true;
                $scope.loginAction = resp.name;
                var token = gapi.auth.getToken();
                token.access_token = token.id_token;
                gapi.auth.setToken(token);
                $scope.$apply();
            }
        });
    }

    $scope.auth = function() {
        $scope.signin(false, $scope.userAuthed);
    };

    $scope.initBackend = function() {
        gapi.client.load('feedbackyourjug', 'v1', function(){
            $scope.backendReady = true;
        }, apiRoot);
        gapi.client.load('oauth2', 'v2', function(){console.log("signin ready")});
    }

    $window.init= function() {
        $scope.initBackend();
    };
});
