

angular.module('conference').controller('EditTalkController', function($scope, $routeParams, $location, TalkResource , SpeakerResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.talk = new TalkResource(self.original);
            SpeakerResource.queryAll(function(items) {
                $scope.speakersSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.id
                    };
                    if($scope.talk.speakers){
                        $.each($scope.talk.speakers, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.speakersSelection.push(labelObject);
                                $scope.talk.speakers.push(wrappedObject);
                            }
                        });
                        self.original.speakers = $scope.talk.speakers;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Talks");
        };
        TalkResource.get({TalkId:$routeParams.TalkId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.talk);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.talk.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Talks");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Talks");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.talk.$remove(successCallback, errorCallback);
    };
    
    $scope.speakersSelection = $scope.speakersSelection || [];
    $scope.$watch("speakersSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.talk) {
            $scope.talk.speakers = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.talk.speakers.push(collectionItem);
            });
        }
    });
    
    $scope.get();
});