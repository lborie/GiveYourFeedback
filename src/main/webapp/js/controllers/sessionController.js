"use strict";

/**
 * Controller of the conference page
 */
feedbackApp.controller("sessionController", function ($scope, SessionService, $routeParams, UserService) {

    if ($routeParams.idConference && $routeParams.idSession) {
        SessionService.getSession($routeParams.idSession)
            .success(function (resp) {
                $scope.session = resp;
            });
    }

    $scope.newComment = function () {
        SessionService.newComment({
            comment: {
                authorEmail: UserService.getUser().email,
                content: $scope.newComment.content,
                authorNickname: UserService.getUser().name
            },
            sessionId: $routeParams.idSession,
            nickName: UserService.getUser().name
        }).execute(function (resp) {
                $scope.session = resp.result;
                $scope.$apply();
            });
    };

    $scope.isLogged = function() {
        return UserService.isLogged();
    };
});