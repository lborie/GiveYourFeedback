"use strict";

/**
 * Controller of the conference page
 */
feedbackApp.controller("importController", function ($scope, $http, ConferenceService, UserService) {
    gapi.load('picker', {'callback': $scope.createPicker});

    $scope.conference = {};

    // Create and render a Picker object for searching images.
    $scope.createPicker = function() {
        var spreadSheetView = google.picker.ViewId.SPREADSHEETS;
        var view = new google.picker.View(google.picker.ViewId.DOCS);
        //view.setMimeTypes("image/png,image/jpeg,image/jpg");
        var picker = new google.picker.PickerBuilder()
            .setAppId('give-your-feedback')
            .addView(spreadSheetView)
            .setDeveloperKey('AIzaSyDSqZ_0qvYoL5q9CFho_gV4fYQ5Fx_6P0U')
            .setCallback($scope.pickerCallback)
            .build();
            //.setOAuthToken(AUTH_TOKEN) //Optional: The auth token used in the current Drive API session.
            //.enableFeature(google.picker.Feature.NAV_HIDDEN)
            //.enableFeature(google.picker.Feature.MULTISELECT_ENABLED)

            //.addView(new google.picker.DocsUploadView())

        picker.setVisible(true);
    }

    // A simple callback implementation.
    $scope.pickerCallback = function(data) {
        if (data.action == google.picker.Action.PICKED) {
            var chosenDoc = data.docs[0];
            $scope.conference.spreadSheetId = chosenDoc.id;
            $scope.conference.spreadSheetUrl = chosenDoc.url;
            $scope.conference.userToken = UserService.getToken().originalAccessToken;
//            $scope.conference.userToken = UserService.getToken().access_token;
            $scope.spreadsheetIcon = chosenDoc.iconUrl;
            $scope.spreadsheetName = chosenDoc.name;
            $scope.$apply();

            console.log('https://spreadsheets.google.com/feeds/spreadsheets/'+ chosenDoc.id + '?access_token=' + $scope.conference.userToken);

        }
    }

    $scope.import = function() {
        ConferenceService.insertConference($scope.conference).execute(function(resp){
            console.log(resp);
        })
    }

});