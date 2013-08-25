"use strict";

/**
 * Controller of the conference page
 */
feedbackApp.controller("conferenceController" ,function ($scope, ConferenceService) {

    $scope.project = 'Start Endpoints';

    ConferenceService.fetch().success(function(resp){
       $scope.conferences = resp.items;
    });

    $scope.displayConference = function(conference) {
        //TODO
    }

});