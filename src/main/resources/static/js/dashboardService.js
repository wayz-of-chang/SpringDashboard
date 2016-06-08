app.factory('dashboards', function($http) {
    var dashboard = {};

    dashboard.dashboards = {};

    dashboard.create = function(data, callback) {
        var cookie = JSON.parse($.cookie('csrf'));
        return $http.post('/dashboards/create', data, {headers: {'X-CSRF-TOKEN':cookie.csrf}}).then(function(response) {
            console.log(response);
            //success
            dashboard.set_dashboard(response.data.data);
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    dashboard.query_dashboards = function(data, callback) {
        var cookie = JSON.parse($.cookie('csrf'));
        return $http.post('/dashboards/get', data, {headers: {'X-CSRF-TOKEN': cookie.csrf}}).then(function(response) {
            console.log(response);
            //success
            dashboard.set_dashboards(response.data.data);
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    dashboard.set_dashboard = function(data) {
        dashboard.dashboards[data.id] = {
            id: data.id,
            name: data.name
        };
    };

    dashboard.set_property = function(id, key, value) {
        dashboard.dashboards[id][key] = value;
    };

    dashboard.set_dashboards = function(data) {
        dashboard.dashboards = data;
    };

    dashboard.get_dashboard = function(id) {
        return dashboard.dashboards[id];
    };

    dashboard.get_property = function(id, key) {
        return dashboard.dashboards[id][key];
    };

    dashboard.get_dashboards = function() {
        return dashboard.dashboards;
    };

    return dashboard;
});