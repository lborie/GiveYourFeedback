"use strict";

/**
 * Controller of the home page
 */
feedbackApp.controller("mainController" ,function ($scope, $window, UserService) {

    $scope.signin = function(mode, callback) {
        if (!$scope.logged) {
            gapi.auth.authorize({
                    client_id: '700903555117-17gcl1qf309d5meq269ffusooto7o03g.apps.googleusercontent.com',
                    scope: 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email',
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
                UserService.setUser(resp);
                $scope.loginAction = resp.name;
                var token = gapi.auth.getToken();
                token.access_token = token.id_token;
                gapi.auth.setToken(token);
            } else {
                $scope.loginAction = 'Login';
                $scope.logged = false;
            }
            $scope.$apply();
        });
    }

    $scope.auth = function() {
        $scope.signin(false, $scope.userAuthed);
    };

    $scope.initBackend = function() {
        gapi.client.load('feedbackyourjug', 'v1', function(){
            $scope.backendReady = true;
        }, apiRoot);
        gapi.client.load('oauth2', 'v2', function(){
            console.log("signin ready")
            $scope.signin(true, $scope.userAuthed);
        });
    }

    $window.init= function() {
        $scope.initBackend();
    };
});

feedbackApp.controller("homeController" ,function ($scope, $window, UserService) {

});
