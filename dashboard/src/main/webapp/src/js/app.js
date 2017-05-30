function getBaseUrl() {
    var url;
    if (window.location.port === "8000") { /* Local debugging */
        url = "http://localhost:8083";
    } else {
        url = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
    }
    return url;
}
var baseUrl = getBaseUrl();

angular.module('dashboardApp', ['ngResource'])
    .controller('LogController', function($scope, $interval, logService) {
        $scope.logs = [];
        $scope.loaded = new Date();
        var loadData = function() {
            logService.get(function(data) {
                $scope.logs = data;
                $scope.loaded = new Date();
            });
        };
        loadData();
        $interval(loadData, 30 * 1000);
    })
    .controller('MetricController', function($scope, $interval, metricService) {
        $scope.metrics = {};
        $scope.loaded = new Date();
        var loadData = function() {
            metricService.get(function(data) {
                $scope.metrics = data;
                $scope.loaded = new Date();
            });
        };
        loadData();
        $interval(loadData, 30 * 1000);
    })
    .service('logService', ['$resource', function($resource) {
        var logResource = $resource(baseUrl+"/api/v1/log");
        this.get = function(callback) {
            return logResource.query().$promise.then(function(data) {
                callback(data)
            });
        }
    }])
    .service('metricService', ['$resource', function($resource) {
        var metricResource = $resource(baseUrl+"/api/v1/metric");
        this.get = function(callback) {
            return metricResource.get().$promise.then(function(data) {
                callback(data.toJSON())
            });
        }
    }])
    .filter('reverse', function() {
        return function(items) {
            return items.slice().reverse();
        };
    });