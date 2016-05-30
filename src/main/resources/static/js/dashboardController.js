angular.module('dashboardApp', [])
    .controller('DashboardController', function() {
        var dashboard = this;
        dashboard.current = "1";
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
    }
);