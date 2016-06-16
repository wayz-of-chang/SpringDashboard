app.controller('DashboardController', function($scope, service) {
    var dashboard = this;
    dashboard.current = "1";
    dashboard.dashboards = {};
    dashboard.new_dashboard = {
        name: '',
        error: false,
        error_message: ''
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
        dashboard.current = id;
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
                setTimeout(function() { dashboard.new_dashboard.success = false; $('#new_dashboard_modal').modal('hide'); }, 5000);
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
    dashboard.delete = function() {
        $.each(dashboard.dashboards, function(index, value) {
            if (value.id == dashboard.current) {
                dashboard.dashboards.splice(index, 1);
                return false;
            }
        });
        if (dashboard.dashboards.length > 0) {
            dashboard.select(dashboard.dashboards[0].id);
        }
    };
    dashboard.open_edit_popup = function() {
    };

    $scope.$watch(function(scope) { return service.get_dashboards(); },
        function(new_val, old_val) { dashboard.dashboards = new_val; }
    );
    $scope.$watch(function(scope) { return service.get_user_settings().current_dashboard; },
        function(new_val, old_val) { dashboard.current = new_val; }
    );
});