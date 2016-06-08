app.controller('DashboardController', function(dashboards, users) {
    var dashboard = this;
    dashboard.current = "1";
    dashboard.index = "4";
    dashboard.dashboards = dashboards.get_dashboards();
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
            userId: users.get_property('id'),
            name: "New Dashboard"
        };
        dashboards.create(data, function(response) {
            dashboard.select(response.data.data.id);
            dashboard.dashboards[dashboard.current] = dashboards.get_dashboard(dashboard.current);
        });
    };
    dashboard.copy = function() {
        dashboard.dashboards.push({
            id: dashboard.index,
            name: dashboard.selected.name
        });
        dashboard.select(dashboard.index);
        dashboard.index += 1;
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
});