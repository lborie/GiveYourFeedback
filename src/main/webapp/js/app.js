"use strict";

var feedbackApp = angular.module('feedbackApp', []);
var ROOT_API = '/_ah/api/feedbackyourjug/v1';

/**
 * Router
 */
feedbackApp.config(function($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: 'views/home.html',
            controller : 'homeController'
        })
        .when('/conference',{
            templateUrl: 'views/conferenceList.html',
            controller: 'conferenceController'
        })
        .when('/conference/:idConference',{
            templateUrl: 'views/conference.html',
            controller: 'conferenceController'
        })
        .when('/conference/:idConference/:idSession',{
            templateUrl: 'views/session.html',
            controller: 'conferenceController'
        })
        .otherwise({
            redirectTo: '/home'
        });
});