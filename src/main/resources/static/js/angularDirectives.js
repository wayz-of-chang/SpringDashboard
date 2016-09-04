var app = angular.module('dashboardApp');
app.directive('repeatFinal', [function() {
    return function(scope) {
        if (scope.$last) {
            scope.$emit('end-repeat');
        }
    };
}]);