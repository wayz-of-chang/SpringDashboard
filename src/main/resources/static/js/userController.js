app.controller('UserController', function(users, dashboards) {
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
    user.logged_in = false;
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
        users.create(data, function(response) {
            if (response.status >= 200 && response.status < 300) {
                user.success = true;
                user.success_message = "User successfully registered: " + response.data.data.username;
                setTimeout(function() { user.success = false; }, 5000);
            } else {
                user.error = true;
                user.error_message = response.data.error + ": " + response.data.message;
            }
        });
    };
    user.login = function() {
        user.mode.login = true;
        user.mode.register = false;
        if (!user.validate()) {
            return false;
        }
        var data = user.user;
        user.success = false;
        user.error = false;
        users.login(data, function(response) {
            if (response.status >= 200 && response.status < 300) {
                user.logged_in = true;
                var cookie = JSON.stringify({csrf: response.headers('X-CSRF-TOKEN')});
                $.cookie('csrf', cookie);
                user.success = true;
                user.success_message = "User successfully logged in: " + response.data.data.username;
                var data = {
                    userId: users.get_property('id')
                };
                setTimeout(function() { user.success = false; }, 5000);
                dashboards.query_dashboards(data, function() {});
            } else {
                user.error = true;
                user.error_message = response.data.error + ": " + response.data.message;
            }
        });
    };
    user.validate = function() {
        if (user.user.username == "" || user.user.password == "") {
            user.error = true;
            user.error_message = "Username and Password cannot be blank."
            return false;
        }
        if (user.mode.register) {
            if (user.user.email == "" || user.roles.indexOf(user.user.role) == -1 ) {
                user.error = true;
                user.error_message = "Email and Role cannot be blank."
                return false;
            }
            if (user.user.password != user.confirm_password) {
                user.error = true;
                user.error_message = "Please make sure the password matches."
                return false;
            }
        }
        return true;
    }
});