"use strict";

/**
 * Controller of the conference page
 */
feedbackApp.controller("conferenceController", function ($scope, $routeParams, $location, ConferenceService, SessionService, CacheService) {

    $scope.project = 'Start Endpoints';

    if ($routeParams.idConference) {
        if (CacheService.getSessions($routeParams.idConference)) {
            var sessionsCache = CacheService.getSessions($routeParams.idConference);
            $scope.sessions = sessionsCache.sessions;
            $scope.locations = sessionsCache.locations;
            $scope.slots = sessionsCache.slots;
            $scope.location1 = $scope.locations[1];
            $scope.location2 = $scope.locations[0];
        } else {
            $('.alert-info').removeClass("hide");
            SessionService.getSessions($routeParams.idConference)
                .success(function (resp) {
                    $('.alert-info').addClass("hide");
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
                    CacheService.setSessions($routeParams.idConference,{
                        sessions : $scope.sessions,
                        locations : $scope.locations,
                        slots : $scope.slots
                    });
                    $scope.location1 = $scope.locations[1];
                    $scope.location2 = $scope.locations[0];
                })
        }
    } else {
        if (CacheService.getConferences()) {
            $scope.conferences = CacheService.getConferences();
        } else {
            $('.alert-info').removeClass("hide");
            ConferenceService.fetch().success(function (resp) {
                $('.alert-info').addClass("hide");
                $scope.conferences = resp.items;
                CacheService.setConferences(resp.items);
            });
        }
    }
});