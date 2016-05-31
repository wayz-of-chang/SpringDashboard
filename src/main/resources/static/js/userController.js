app.controller('UserController', function($http) {
    var user = this;
    user.show_password = false;
    user.confirm_password = "";
    user.error = false;
    user.error_message = "";
    user.success = false;
    user.success_message = "";
    user.roles = ['R_USER', 'RW_USER'];
    user.mode = {
        register: false,
        login: true
    };
    user.user = {
        id: '',
        username: '',
        password: '',
        email: '',
        role: ''
    };
    user.register = function() {
        if (user.mode.register) {
            user.create();
        } else {
            user.mode.register = true;
            user.mode.login = false;
        }
    };
    user.create = function() {
        if (!user.validate()) {
            return false;
        }
        var data = {
            username: user.user.username,
            password: user.user.password,
            email: user.user.email,
            role: user.user.role
        };
        user.success = false;
        user.error = false;
        $http.post('/users/create', data).then(function(response) {
            //success
            user.user.id = response.data.data.id;
            user.success = true;
            user.success_message = "User successfully registered: " + response.data.data.username;
            setTimeout(function() {user.success = false;}, 5000);
        }, function(response) {
            //fail
            user.error = true;
            user.error_message = response.data.error + ": " + response.data.message;
        })
    };
    user.login = function() {
        user.mode.login = true;
        user.mode.register = false;
        var data = user.user;
        $http.post('/users/login', data).then(function(response) {

        }, function(response) {

        })
    };
    user.validate = function() {
        if (user.user.username == "" || user.user.password == "") {
            return false;
        }
        if (user.mode.register) {
            if (user.user.email == "" || user.roles.indexOf(user.user.role) == -1 ) {
                return false;
            }
            if (user.user.password != user.confirm_password) {
                return false;
            }
        }
        return true;
    }
});