"use strict";

/**
 * Controller of the conference page
 */
feedbackApp.controller("conferenceController" ,function ($scope, $routeParams, ConferenceService) {

    $scope.project = 'Start Endpoints';

    ConferenceService.fetch().success(function(resp){
       $scope.conferences = resp.items;
    });

    $scope.displayConference = function(conference) {
        console.log($routeParams.conferenceId);
    }

    $scope.displaySession = function(session) {
        //TODO
    }
});