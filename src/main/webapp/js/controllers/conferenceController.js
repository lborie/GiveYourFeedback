"use strict";

/**
 * Controller of the conference page
 */
feedbackApp.controller("conferenceController", function ($scope, $routeParams, $location, ConferenceService, SessionService) {

    $scope.project = 'Start Endpoints';

    if ($routeParams.idConference) {
        SessionService.getSessions($routeParams.idConference)
            .success(function (resp) {
                $scope.sessions = {};
                $scope.locations = [];
                $scope.slots = [];
                for (var i = 0; i < resp.items.length; i++) {
                    var currentSession = resp.items[i];
                    currentSession.sessionUrl = currentSession.idConference.toString() + '/' + currentSession.id.toString();
                    if (!$scope.sessions[currentSession.startTime]){
                        $scope.sessions[currentSession.startTime] = {} ;
                    }
                    if (!$scope.sessions[currentSession.startTime][currentSession.location]){
                        $scope.sessions[currentSession.startTime][currentSession.location] = {};
                    }
                    $scope.sessions[currentSession.startTime][currentSession.location] = currentSession;
                    if ($scope.locations.indexOf(currentSession.location) == -1){
                        $scope.locations.push(currentSession.location);
                    }
                    if ($scope.slots.indexOf(currentSession.startTime) == -1){
                        $scope.slots.push(currentSession.startTime);
                    }
                }
            })
    } else {
        ConferenceService.fetch().success(function (resp) {
            $scope.conferences = resp.items;
        });
    }
});