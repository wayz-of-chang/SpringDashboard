app.controller('UserController', function($scope, $window, service) {
    var user = this;
    user.show_password = false;
    user.confirm_password = "";
    user.remember_me = false;
    user.error = false;
    user.error_message = "";
    user.success = false;
    user.success_message = "";
    user.roles = ['R_USER', 'RW_USER'];
    user.mode = {
        register: false,
        login: true
    };
    user.logged_in = service.logged_in;
    user.user = service.user;
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
        service.create_user(data, function(response) {
            if (response.status >= 200 && response.status < 300) {
                user.success = true;
                user.success_message = "User successfully registered: " + response.data.data.username;
                setTimeout(function() { $('body').trigger('click'); user.success = false; $scope.$digest(); }, 3000);
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
        service.login(data, function(response) {
            if (response.status >= 200 && response.status < 300) {
                user.logged_in = true;
                user.success = true;
                user.success_message = "User successfully logged in: " + response.data.data.username;
                if (user.remember_me) {
                    var remember_me = JSON.stringify({remember_me: true});
                    $.cookie('remember_me', remember_me);
                } else {
                    $.cookie('u', null);
                    $.cookie('p', null);
                    $.cookie('remember_me', null);
                }
                setTimeout(function() { $('body').trigger('click'); user.success = false; $scope.$digest(); }, 3000);
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
    };
    user.open_settings_popup = function() {
        $('#user_settings_modal').modal('show');
    };
    user.close_settings_popup = function() {
        $('#user_settings_modal').modal('hide');
    };

    user.user_settings = function() {
        return service.user_settings;
    };
    user.dashboards = function() {
        return service.dashboards;
    };
    user.dashboard_keys = function() {
        return Object.keys(user.dashboards());
    };
    user.monitors = function() {
        return service.monitors;
    };
    user.monitor_order = function() {
        return service.user_settings.monitor_order[service.user_settings.current_dashboard];
    };
    user.export = function() {
        service.export_user(function(response) {
            //console.log(response.data.data);
            var export_file = new Blob([JSON.stringify(response.data.data, null, 2)], {type: "application/json"});
            var reader = new FileReader();
            reader.onloadend = function(e) {
                $window.open(reader.result);
            }
            reader.readAsDataURL(export_file);
            return response.data.data;
        });
    };

    $scope.$watch(function(scope) { return service.get_login_status(); },
        function(new_val, old_val) { user.logged_in = new_val; }
    );
});