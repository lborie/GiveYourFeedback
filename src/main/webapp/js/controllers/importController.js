//"use strict";

/**
 * Controller of the conference page
 */
feedbackApp.controller("importController", function ($scope, $http, ConferenceService, UserService) {
    gapi.load('picker', {'callback': $scope.createPicker});

    $scope.conference = {};

    // Init file picker for Illustration
    $("#conferenceIllustration").bind("change",function(event){
        try{
            var fileData = event.target.files[0];
        }
        catch(e) {
            //'Insert Object' selected from the API Commands select list
            //Display insert object button and then exit function
            conferenceIllustration.style.display = 'block';
            return;
        }
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

            //Note: gapi.client.storage.objects.insert() can only insert
            //small objects (under 64k) so to support larger file sizes
            //we're using the generic HTTP request method gapi.client.request()
            // HACK
            var newToken = UserService.getToken();
            newToken.access_token = newToken.originalAccessToken;
            gapi.auth.setToken(newToken);
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
                    var token = gapi.auth.getToken();
                    token.originalAccessToken = token.access_token;
                    token.access_token = token.id_token;
                    UserService.setToken(token);
                    gapi.auth.setToken(token);
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
            console.log(resp);
        })
    }

});