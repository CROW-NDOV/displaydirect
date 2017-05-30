function getBaseUrl() {
    var url;
    if (window.location.port === "8000") { /* Local debugging */
        url = "http://localhost:8084";
    } else {
        url = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
    }
    return url;
}
var baseUrl = getBaseUrl();

angular.module('stopSystemApp', ['ngResource'])
    .controller('DepartureController', function($scope, $interval, departureService) {
        $scope.departures = [];
        $scope.time = new Date();
        var loadData = function() {
            departureService.get(function(data) {
                $scope.departures = data;
            });
        };
        var setTime = function() {
            $scope.time = new Date();
        };
        $scope.isNow = function(time) {
            var min = Math.round((new Date(time * 1000) - new Date())/1000/60);
            return min <= 0;
        };
        $scope.getSeconds = function (time) {
            return Math.round((new Date(time * 1000) - new Date())/1000);
        }
        loadData();
        $interval(loadData, 1000);
        $interval(setTime, 1000);
    })
    .service('departureService', ['$resource', function($resource) {
        var departureResource = $resource(baseUrl+"/api/v1/departures");
        this.get = function(callback) {
            return departureResource.query().$promise.then(function(data) {
                callback(data)
            });
        }
    }])
    .filter('reverse', function() {
        return function(items) {
            return items.slice().reverse();
        };
    })
    .filter('minutesFromNow', ['$sce', function($sce) {
        return function(time) {
            var min = Math.round((new Date(time * 1000) - new Date())/1000/60);
            return min + " min";
        };
    }]);