//"use strict";

/**
 * Controller of the conference page
 */
feedbackApp.controller("importController", function ($scope, $http, ConferenceService) {
    gapi.load('picker', {'callback': $scope.createPicker});

    $scope.conference = {};

    // Init file picker for Illustration
    $("#conferenceIllustration").bind("change",function(event){
        var fileData = event.target.files[0];

        const boundary = '-------314159265358979323846';
        const delimiter = "\r\n--" + boundary + "\r\n";
        const close_delim = "\r\n--" + boundary + "--";

        var reader = new FileReader();
        reader.readAsBinaryString(fileData);
        reader.onload = function(e) {
            var contentType = fileData.type || 'application/octet-stream';
            var metadata = {
                'name': fileData.name + new Date().getTime().toString(),
                'mimeType': contentType,
                'acl': [{
                    "kind": "storage#objectAccessControl",
                    "entity": 'allUsers',
                    "role": 'READER'
                }]
            };

            var base64Data = btoa(reader.result);
            var multipartRequestBody =
                delimiter +
                    'Content-Type: application/json;x-goog-acl: public-read\r\n\r\n' +
                    JSON.stringify(metadata) +
                    delimiter +
                    'Content-Type: ' + contentType + '\r\n' +
                    'Content-Transfer-Encoding: base64\r\n' +
                    '\r\n' +
                    base64Data +
                    close_delim;

            var request = gapi.client.request({
                'path': '/upload/storage/'+API_VERSION+'/b/' + BUCKET + '/o',
                'method': 'POST',
                'params': {'uploadType': 'multipart'},
                'headers': {
                    'Content-Type': 'multipart/mixed; boundary="' + boundary + '"'
                },
                'body': multipartRequestBody});
            //Remove the current API result entry in the main-content div

            try{
                //Execute the insert object request
                request.execute(function(resp) {
                    console.log(resp);
                    $scope.conference.baniereUrl = resp.mediaLink;
                    $scope.$apply();
                });
            }
            catch(e) {
                alert('An error has occurred: ' + e.message);
            }
        }
    });

    // Create and render a Picker object for searching images.
    $scope.createPicker = function() {
        var spreadSheetView = google.picker.ViewId.SPREADSHEETS;

        var picker = new google.picker.PickerBuilder()
            .setAppId('give-your-feedback')
            .addView(spreadSheetView)
            .setDeveloperKey('AIzaSyDSqZ_0qvYoL5q9CFho_gV4fYQ5Fx_6P0U')
            .setCallback($scope.pickerCallback)
            .build();

        picker.setVisible(true);
    }

    // A simple callback implementation.
    $scope.pickerCallback = function(data) {
        if (data.action == google.picker.Action.PICKED) {
            var chosenDoc = data.docs[0];
            $scope.conference.spreadSheetId = chosenDoc.id;
            $scope.conference.spreadSheetUrl = chosenDoc.url;
            $scope.conference.userToken = gapi.auth.getToken().access_token;
            $scope.spreadsheetIcon = chosenDoc.iconUrl;
            $scope.spreadsheetName = chosenDoc.name;
            $scope.$apply();
        }
    }

    $scope.import = function() {
        $("#importEnCours").removeClass("hide");
        $("#importSuccess").addClass("hide");
        $("#importFail").addClass("hide");

        ConferenceService.insertConference($scope.conference).execute(function(resp){
            $("#importEnCours").addClass("hide");
            if (resp.code) {
                $("#importFail").removeClass("hide");
            } else {
                $("#importSuccess").removeClass("hide");
            }
        });
    }
});