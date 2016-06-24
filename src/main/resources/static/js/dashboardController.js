app.controller('DashboardController', function($scope, service) {
    var dashboard = this;
    dashboard.current = "1";
    dashboard.dashboards = {};
    dashboard.new_dashboard = {
        name: '',
        error: false,
        error_message: '',
        success: false,
        success_message: ''
    };
    dashboard.edit_dashboard = {
        error: false,
        error_message: '',
        success: false,
        success_message: ''
    };
    dashboard.delete_dashboard = {
        error: false,
        error_message: '',
        success: false,
        success_message: ''
    };
    dashboard.unselected = function() {
        var unselected = [];
        $.each(dashboard.dashboards, function(index, value) {
            if (index != dashboard.current) {
                unselected.push(index);
            }
        });
        return unselected;
    };
    dashboard.select = function(id) {
        service.get_user_settings().current_dashboard = id;
        dashboard.new_dashboard.error = false;
        dashboard.edit_dashboard.error = false;
        dashboard.delete_dashboard.error = false;
        if (id > '') {
            var data = {
                dashboardId: id
            };
            service.query_monitors(data, function() {});
        } else {
            service.clear_monitors();
        }
    };
    dashboard.open_new_popup = function() {
        $('#new_dashboard_modal').modal('show');
    };
    dashboard.create_new_dashboard = function() {
        var data = {
            userId: service.get_user_property('id'),
            name: dashboard.new_dashboard.name
        };
        service.create_dashboard(data, function(response) {
            if (response.status >= 200 && response.status < 300) {
                dashboard.new_dashboard.error = false;
                dashboard.new_dashboard.error_message = '';
                dashboard.new_dashboard.success = true;
                dashboard.new_dashboard.success_message = "Successfully created dashboard: " + response.data.data.name;
                dashboard.select(response.data.data.id);
                setTimeout(function() { dashboard.new_dashboard.success = false; $('#new_dashboard_modal').modal('hide'); }, 3000);
            } else {
                dashboard.new_dashboard.error = true;
                dashboard.new_dashboard.error_message = response.data.error + ": " + response.data.message;
                dashboard.new_dashboard.success = false;
            }
        });
    };
    dashboard.cancel_new_dashboard_modal = function() {
        $('#new_dashboard_modal').modal('hide');
    };
    dashboard.copy = function() {
        var data = {
            userId: service.get_user_property('id'),
            name: dashboard.dashboards[dashboard.current].name
        };
        service.create_dashboard(data, function(response) {
            if (response.status >= 200 && response.status < 300) {
                dashboard.select(response.data.data.id);
            } else {
            }
        });
    };
    dashboard.open_edit_popup = function() {
        $('#edit_dashboard_modal').modal('show');
    };
    dashboard.update = function() {
        var data = {
            userId: service.get_user_property('id'),
            id: dashboard.current,
            name: dashboard.dashboards[dashboard.current].name
        };
        service.edit_dashboard(data, function(response) {
            if (response.status >= 200 && response.status < 300) {
                dashboard.edit_dashboard.error = false;
                dashboard.edit_dashboard.error_message = '';
                dashboard.edit_dashboard.success = true;
                dashboard.edit_dashboard.success_message = "Successfully updated dashboard: " + response.data.data.name;
                dashboard.select(response.data.data.id);
                setTimeout(function() { dashboard.edit_dashboard.success = false; $('#edit_dashboard_modal').modal('hide'); }, 3000);
            } else {
                dashboard.edit_dashboard.error = true;
                dashboard.edit_dashboard.error_message = response.data.error + ": " + response.data.message;
                dashboard.edit_dashboard.success = false;
            }
        });
    };
    dashboard.cancel_edit_dashboard_modal = function() {
        $('#edit_dashboard_modal').modal('hide');
    };
    dashboard.confirm_delete_popup = function() {
        $('#delete_dashboard_modal').modal('show');
    };
    dashboard.delete = function() {
        var data = {
            userId: service.get_user_property('id'),
            id: dashboard.current
        };
        service.delete_dashboard(data, function(response) {
            if (response.status >= 200 && response.status < 300) {
                dashboard.delete_dashboard.error = false;
                dashboard.delete_dashboard.error_message = '';
                dashboard.delete_dashboard.success = true;
                dashboard.delete_dashboard.success_message = "Successfully updated dashboard: " + response.data.data.name;
                if (Object.keys(dashboard.dashboards).length > 0) {
                    dashboard.select(Object.keys(dashboard.dashboards)[0]);
                } else {
                    dashboard.select('');
                }
                setTimeout(function() {
                    dashboard.delete_dashboard.success = false;
                    $('#delete_dashboard_modal').modal('hide');
                }, 3000);
            } else {
                dashboard.delete_dashboard.error = true;
                dashboard.delete_dashboard.error_message = response.data.error + ": " + response.data.message;
                dashboard.delete_dashboard.success = false;
            }
        });
    };
    dashboard.cancel_delete_dashboard_modal = function() {
        $('#delete_dashboard_modal').modal('hide');
    };

    $scope.$watch(function(scope) { return service.get_dashboards(); },
        function(new_val, old_val) { dashboard.dashboards = new_val; }
    );
    $scope.$watch(function(scope) { return service.get_user_settings().current_dashboard; },
        function(new_val, old_val) { dashboard.current = new_val; }
    );
});