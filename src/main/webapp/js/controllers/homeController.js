"use strict";

/**
 * Controller of the home page
 */
feedbackApp.controller("mainController" ,function ($scope, $window, UserService) {
    $scope.profileUrl = "#";
    $scope.loginAction = 'Login';
    $scope.profileClass="";

    $scope.signin = function(mode, callback) {
        if (!$scope.logged) {
            gapi.auth.authorize({
                    client_id: '700903555117-17gcl1qf309d5meq269ffusooto7o03g.apps.googleusercontent.com',
                    scope: 'https://www.googleapis.com/auth/devstorage.full_control https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/drive.file https://spreadsheets.google.com/feeds https://docs.google.com/feeds',
                    immediate: mode},
                callback
            );
        }
    };

    $scope.userAuthed = function() {
        gapi.client.oauth2.userinfo.get().execute(function(resp) {
            console.log(resp);
            if (!resp.code) {
                UserService.setUser(resp);
                $scope.loginAction = resp.name;
                $scope.profileUrl = resp.link;
                $scope.profilePictureUrl = resp.picture;

                // Cloud Storage
                gapi.client.setApiKey('AIzaSyCbEGwzJkS_ybo9whM_GcAYTsm1RH2DD9U');
                gapi.client.load('storage', API_VERSION);

            } else {
                $scope.loginAction = 'Login';
                $scope.profileUrl = "#";
                $scope.logged = false;
            }
            $scope.$apply();
        });
    };

    $scope.auth = function() {
        if (!UserService.isLogged()){
            $scope.signin(false, $scope.userAuthed);
        }
    };

    $scope.initBackend = function() {
        gapi.client.setApiKey('AIzaSyCbEGwzJkS_ybo9whM_GcAYTsm1RH2DD9U');

        gapi.client.load('giveyourfeedback', 'v2', function(){
            $scope.backendReady = true;
        }, apiRoot);

        gapi.client.load('oauth2', 'v2', function(){
            console.log("signin ready");
            $scope.signin(true, $scope.userAuthed);
        });
    };

    $window.init= function() {
        $scope.initBackend();
    };

    $scope.isLogged = function() {
        return UserService.isLogged();
    };
});

feedbackApp.controller("homeController" ,function ($scope, $window, UserService) {

});
