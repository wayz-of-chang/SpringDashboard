app.factory('users', function($http) {
    var user = {};

    user.user = {
        id: '',
        username: '',
        email: '',
        role: ''
    };

    user.create = function(data, callback) {
        return $http.post('/users/create', data, {headers: {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')}}).then(function(response) {
            console.log(response);
            //success
            user.set_user(response.data.data);
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    user.login = function(data, callback) {
        return $http.post('/users/login', data, {headers: {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')}}).then(function(response) {
            console.log(response);
            //success
            user.set_user(response.data.data);
            return callback(response);
        }, function(response) {
            console.log(response);
            //fail
            return callback(response);
        });
    };

    user.set_user = function(data) {
        user.user.id = data.id;
        user.user.username = data.username;
        user.user.email = data.email;
        user.user.role = data.role;
    };

    user.set_property = function(key, value) {
        user.user[key] = value;
    };

    user.get_user = function() {
        return user.user;
    };

    user.get_property = function(key) {
        return user.user[key];
    };

    return user;
});