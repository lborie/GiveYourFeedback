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
        $scope.newComment.authorEmail = UserService.getUser().email;
        SessionService.newComment({
            comment: $scope.newComment,
            sessionId: $routeParams.idSession,
            nickName: UserService.getUser().name
        }).execute(function (resp) {
                $scope.session = resp;
            });
    }

    $scope.isLogged = UserService.isLogged();
});