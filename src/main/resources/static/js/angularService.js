app.factory('service', function($http) {
    var service = {};

    service.user = {
        id: '',
        username: '',
        email: '',
        role: ''
    };
    service.dashboards = {};
    service.monitors = {};
    service.user_settings = {
        current_dashboard: ''
    };

    service.create_user = function(data, callback) {
        return $http.post('/users/create', data, {headers: {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')}}).then(function(response) {
            console.log(response);
            //success
            service.set_user(response.data.data);
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    service.login = function(data, callback) {
        return $http.post('/users/login', data, {headers: {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')}}).then(function(response) {
            console.log(response);
            //success
            service.set_user(response.data.data);
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    service.set_user = function(data) {
        service.user.id = data.id;
        service.user.username = data.username;
        service.user.email = data.email;
        service.user.role = data.role;
    };

    service.set_user_property = function(key, value) {
        service.user[key] = value;
    };

    service.get_user = function() {
        return service.user;
    };

    service.get_user_property = function(key) {
        return service.user[key];
    };

    service.create_dashboard = function(data, callback) {
        var cookie = JSON.parse($.cookie('csrf'));
        return $http.post('/dashboards/create', data, {headers: {'X-CSRF-TOKEN':cookie.csrf}}).then(function(response) {
            console.log(response);
            //success
            service.set_dashboard(response.data.data);
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    service.query_dashboards = function(data, callback) {
        var cookie = JSON.parse($.cookie('csrf'));
        return $http.post('/dashboards/get', data, {headers: {'X-CSRF-TOKEN': cookie.csrf}}).then(function(response) {
            console.log(response);
            //success
            service.set_dashboards(response.data.data);
            service.user_settings.current_dashboard = Object.keys(service.get_dashboards())[0];
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    service.edit_dashboard = function(data, callback) {
        var cookie = JSON.parse($.cookie('csrf'));
        return $http.post('/dashboards/edit', data, {headers: {'X-CSRF-TOKEN': cookie.csrf}}).then(function(response) {
            console.log(response);
            //success
            service.set_dashboard(response.data.data);
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    service.delete_dashboard = function(data, callback) {
        var cookie = JSON.parse($.cookie('csrf'));
        return $http.post('/dashboards/delete', data, {headers: {'X-CSRF-TOKEN': cookie.csrf}}).then(function(response) {
            console.log(response);
            //success
            delete service.dashboards[response.data.data.id];
            service.user_settings.current_dashboard = Object.keys(service.get_dashboards())[0];
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    service.create_monitor = function(data, callback) {
        var cookie = JSON.parse($.cookie('csrf'));
        return $http.post('/monitors/create', data, {headers: {'X-CSRF-TOKEN':cookie.csrf}}).then(function(response) {
            console.log(response);
            //success
            service.set_monitor(response.data.data);
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    service.query_monitors = function(data, callback) {
        var cookie = JSON.parse($.cookie('csrf'));
        return $http.post('/monitors/get', data, {headers: {'X-CSRF-TOKEN': cookie.csrf}}).then(function(response) {
            console.log(response);
            //success
            service.set_monitors(response.data.data);
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    service.set_dashboard = function(data) {
        service.dashboards[data.id] = {
            id: data.id,
            name: data.name
        };
    };

    service.set_dashboard_property = function(id, key, value) {
        service.dashboards[id][key] = value;
    };

    service.set_dashboards = function(data) {
        $.each(data, function(index, value) {
            service.set_dashboard(value);
        });
    };

    service.get_dashboard = function(id) {
        return service.dashboards[id];
    };

    service.get_dashboard_property = function(id, key) {
        return service.dashboards[id][key];
    };

    service.get_dashboards = function() {
        return service.dashboards;
    };

    service.get_user_settings = function() {
        return service.user_settings;
    };

    service.set_monitor = function(data) {
        service.monitors[data.id] = {
            id: data.id,
            name: data.name
        };
    };

    service.set_monitor_property = function(id, key, value) {
        service.monitors[id][key] = value;
    };

    service.set_monitors = function(data) {
        $.each(data, function(index, value) {
            service.set_monitor(value);
        });
    };

    service.get_monitor = function(id) {
        return service.monitors[id];
    };

    service.get_monitor_property = function(id, key) {
        return service.monitors[id][key];
    };

    service.get_monitors = function() {
        return service.monitors;
    };

    return service;
});