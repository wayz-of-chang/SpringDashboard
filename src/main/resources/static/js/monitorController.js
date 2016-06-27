app.controller('MonitorController', function($scope, service) {
    var monitor = this;
    monitor.current = "1";
    monitor.monitoring = false;
    monitor.monitors = {};
    monitor.flipped = {};
    monitor.new_monitor = {
        name: '',
        error: false,
        error_message: '',
        success: false,
        success_message: ''
    };
    monitor.edit_monitor = {
        error: false,
        error_message: '',
        success: false,
        success_message: ''
    };
    monitor.delete_monitor = {
        error: false,
        error_message: '',
        success: false,
        success_message: ''
    };
    monitor.stats = {};
    monitor.add_monitor = function() {
        var data = {
            dashboardId: monitor.current_dashboard()
        };
        service.create_monitor(data, function(response) {
            if (response.status >= 200 && response.status < 300) {
                monitor.new_monitor.error = false;
                monitor.new_monitor.error_message = '';
                monitor.new_monitor.success = true;
                monitor.new_monitor.success_message = "Successfully created monitor";
                setTimeout(function() { monitor.new_monitor.success = false; }, 3000);
            } else {
                monitor.new_monitor.error = true;
                monitor.new_monitor.error_message = response.data.error + ": " + response.data.message;
                monitor.new_monitor.success = false;
            }
        });
    };

    monitor.flip_monitor = function(id) {
        if (monitor.flipped[id] == undefined) {
            monitor.flipped[id] = false;
        }
        if (monitor.flipped[id]) {
            service.update_monitor_settings(id, function(response) {});
        }
        monitor.flipped[id] = !monitor.flipped[id];
    };
    monitor.current_dashboard = function() {
        return service.get_user_settings().current_dashboard;
    };
    monitor.get_results = function(id) {
        if (monitor.stats) {
            return monitor.stats[id];
        }
        return '';
    };

    monitor.toggle_monitoring = function() {
        service.toggle_monitoring_status();
    };

    $scope.$watch(function(scope) { return service.get_monitors(); },
        function(new_val, old_val) { monitor.monitors = new_val; }
    );
    $scope.$watch(function(scope) { return monitor.current_dashboard(); },
        function(new_val, old_val) { service.get_monitors(); }
    );
    $scope.$watch(function(scope) { return service.get_monitor_results(); },
        function(new_val, old_val) { monitor.stats = new_val; }
    );
    $scope.$watch(function(scope) { return service.get_monitoring_status(); },
        function(new_val, old_val) { monitor.monitoring = new_val; }
    );
});