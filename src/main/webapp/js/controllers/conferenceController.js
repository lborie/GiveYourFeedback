"use strict";

/**
 * Controller of the conference page
 */
feedbackApp.controller("conferenceController", function ($scope, $routeParams, $location, ConferenceService, SessionService) {

    $scope.project = 'Start Endpoints';

    if ($routeParams.idConference) {
        if ($routeParams.idSession) {
            SessionService.getSession($routeParams.idSession)
                .success(function (resp) {
                    $scope.session = resp;
                })
        } else {
            SessionService.getSessions($routeParams.idConference)
                .success(function (resp) {
                    $scope.sessions = [];
                    for (var i = 0; i < resp.items.length; i++) {
                        var currentSession = resp.items[i];
                        currentSession.sessionUrl = currentSession.idConference.toString() + '/' + currentSession.id.toString();
                        $scope.sessions.push(currentSession);
                    }
                })
        }
    } else {
        ConferenceService.fetch().success(function (resp) {
            $scope.conferences = resp.items;
        });
    }
});