
angular.module('conference').controller('NewTalkController', function ($scope, $location, locationParser, TalkResource , SpeakerResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.talk = $scope.talk || {};
    
    $scope.speakersList = SpeakerResource.queryAll(function(items){
        $scope.speakersSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.id
            });
        });
    });
    $scope.$watch("speakersSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.talk.speakers = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.talk.speakers.push(collectionItem);
            });
        }
    });


    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Talks/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        TalkResource.save($scope.talk, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Talks");
    };
});