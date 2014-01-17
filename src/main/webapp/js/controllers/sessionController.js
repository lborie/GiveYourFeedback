"use strict";

/**
 * Controller of the conference page
 */
feedbackApp.controller("sessionController", function ($scope, SessionService, $routeParams, UserService) {

    if ($routeParams.idConference && $routeParams.idSession) {
        $scope.idConference = $routeParams.idConference;
        SessionService.getSession($routeParams.idSession)
            .success(function (resp) {
                $scope.session = resp;
            });
    }

    $scope.newComment = function () {
        var nickName = $scope.newComment.authorNickname;
        var finalComment = {
            content: $scope.newComment.content,
            authorNickname: nickName
        };
        if (UserService.isLogged()){
            nickName = UserService.getUser().name;
        }
        SessionService.newComment({
            comment: finalComment,
            sessionId: $routeParams.idSession,
            nickName: nickName
        }).execute(function (resp) {
                $scope.session = resp.result;
                $scope.$apply();
            });
    };

    $scope.isLogged = function() {
        return UserService.isLogged();
    };
});