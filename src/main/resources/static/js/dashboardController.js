app.controller('DashboardController', function($scope, service) {
    var dashboard = this;
    dashboard.current = "1";
    dashboard.dashboards = {};
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
        var data = {
            userId: service.get_user_property('id'),
            name: "New Dashboard"
        };
        service.create_dashboard(data, function(response) {
            dashboard.select(response.data.data.id);
        });
    };
    dashboard.copy = function() {
        var data = {
            userId: service.get_user_property('id'),
            name: dashboard.dashboards[dashboard.current].name
        };
        dashboard.dashboards.push({
            id: dashboard.index,
            name: dashboard.selected.name
        });
        service.create_dashboard(data, function(response) {
            dashboard.select(response.data.data.id);
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