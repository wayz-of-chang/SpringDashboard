app.controller('DashboardController', function($http, users) {
    var dashboard = this;
    dashboard.current = "1";
    dashboard.index = "4";
    dashboard.dashboards = [
        {
            id: '1',
            name: 'Dashboard 1'
        },
        {
            id: '2',
            name: 'Dashboard 2'
        },
        {
            id: '3',
            name: 'Dashboard 3'
        }
    ];
    dashboard.selected = {
        id: '1',
        name: 'Dashboard 1'
    };
    dashboard.unselected = [
        {
            id: '2',
            name: 'Dashboard 2'
        },
        {
            id: '3',
            name: 'Dashboard 3'
        }
    ];
    dashboard.select = function(id) {
        dashboard.current = id;
        var unselected = [];
        $.each(dashboard.dashboards, function(index, value) {
            if (value.id == dashboard.current) {
                dashboard.selected = value;
            } else {
                unselected.push(value);
            }
        });
        dashboard.unselected = unselected;
    };
    dashboard.open_new_popup = function() {
        /*dashboard.dashboards.push({
            id: dashboard.index,
            name: "New Dashboard"
        });
        dashboard.index += 1;*/
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