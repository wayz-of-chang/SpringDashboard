app.controller('MonitorController', function($scope, service) {
    var monitor = this;
    monitor.current = "1";
    monitor.monitoring = false;
    monitor.automatic_reconnect = false;
    monitor.session_expired = true;
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
        id: '',
        name: '',
        error: false,
        error_message: '',
        success: false,
        success_message: ''
    };
    monitor.stats = {};
    monitor.parsers = {};
    monitor.ordered_monitors = function() {
        var ordered = [];
        var monitors = [];
        var user_settings = service.get_user_settings();
        if (user_settings.current_dashboard > '' && user_settings.monitor_order != null && user_settings.monitor_order[user_settings.current_dashboard] != null) {
            ordered = user_settings.monitor_order[user_settings.current_dashboard];
        } else {
            ordered = Object.keys(monitor.monitors);
        }
        $.each(ordered, function(index, value) {
            if (monitor.monitors[value] != null) {
                monitors.push(monitor.monitors[value]);
            }
        });
        return monitors;
    };

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
                setTimeout(function() {
                    monitor.new_monitor.success = false;
                    service.update_monitor_order();
                }, 3000);
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
            service.update_monitor_settings(id, function(response) {
                //monitor.setup_chart(id);
            });
        }
        monitor.flipped[id] = !monitor.flipped[id];
    };
    monitor.current_dashboard = function() {
        return service.get_user_settings().current_dashboard;
    };
    monitor.get_results = function(id) {
        if (monitor.stats) {
            if (monitor.monitors[id].parser_function && monitor.stats[id] && monitor.stats[id].data) {
                var data = monitor.monitors[id].parser_function(monitor.stats[id]);
                monitor.update_chart(id, data);
                return data;
            } else {
                var data = monitor.stats[id];
                return data;
            }
        }
        return '';
    };
    monitor.setup_chart = function(id) {
        if (monitor.monitors[id].chart == 'status') {
            if ($('#chart_' + id).size() == 0) {
                return;
            }
            d3.selectAll("#chart_" + id + " > *").remove();
            var status = loadStatusIndicator("chart_" + id, 'n/a');
            monitor.monitors[id].chart_render = status;
        } else if (monitor.monitors[id].chart == 'gauge') {
            if ($('#chart_' + id).size() == 0) {
                return;
            }
            var config = liquidFillGaugeDefaultSettings();
            config.minValue = 0;
            config.maxValue = 100;
            config.circleThickness = 0.05;
            config.textVertPosition = 0.5;
            config.waveAnimateTime = 1000;
            config.textSize = 0.8;
            d3.selectAll("#chart_" + id + " > *").remove();
            var gauge = loadLiquidFillGauge("chart_" + id, 0, config);
            monitor.monitors[id].chart_config = config;
            monitor.monitors[id].chart_render = gauge;
        } else if (monitor.monitors[id].chart == 'bar') {
            if ($('#chart_' + id).size() == 0) {
                return;
            }
            var config = barChartDefaultSettings();
            config.minValue = 0;
            config.maxValue = 100;
            d3.selectAll("#chart_" + id + " > *").remove();
            var chart = loadBarChart("chart_" + id, [{key: "n/a", value: 0}], config);
            monitor.monitors[id].chart_config = config;
            monitor.monitors[id].chart_render = chart;
        } else {
            monitor.monitors[id].show_raw = true;
        }
    };
    monitor.update_chart = function(id, data) {
        if (monitor.monitors[id].chart == 'status') {
            var value = data.value;
            var status = data.status;
            if (status == 'success') {
                $('#chart_' + id).removeClass('medium high').addClass('low');
            }
            if (status == 'warning') {
                $('#chart_' + id).removeClass('low high').addClass('medium');
            }
            if (status == 'failure') {
                $('#chart_' + id).removeClass('medium low').addClass('high');
            }
            if (monitor.monitors[id].chart_render == null) {
                monitor.setup_chart(id);
                return;
            }
            monitor.monitors[id].chart_render.update(value);
        }
        if (monitor.monitors[id].chart == 'gauge') {
            var value = data.value;
            var max = data.max;
            var unit = data.unit;
            var mediumThreshold = data.mediumThreshold;
            var highThreshold = data.highThreshold;
            if (typeof unit == 'undefined') {
                unit = '';
            }
            if (typeof mediumThreshold == 'undefined') {
                mediumThreshold = 0.5;
            }
            if (typeof highThreshold == 'undefined') {
                highThreshold = 0.9;
            }
            var percentage = 1.0 * value / max;
            if (percentage <= mediumThreshold) {
                $('#chart_' + id).removeClass('medium high').addClass('low');
            }
            if (percentage > mediumThreshold) {
                $('#chart_' + id).removeClass('low high').addClass('medium');
            }
            if (percentage > highThreshold) {
                $('#chart_' + id).removeClass('medium low').addClass('high');
            }
            if (unit == '%') {
                value = Math.round(100 * percentage);
                max = 100;
            }

            if (monitor.monitors[id].chart_config == null) {
                monitor.setup_chart(id);
                return;
            }
            monitor.monitors[id].chart_config.maxValue = max;
            monitor.monitors[id].chart_config.displayUnit = unit;
            monitor.monitors[id].chart_render.update(value);
        }
        if (monitor.monitors[id].chart == 'bar') {
            var values = data.values;
            var max = data.max;
            var unit = data.unit;
            var mediumThreshold = data.mediumThreshold;
            var highThreshold = data.highThreshold;
            if (typeof unit == 'undefined') {
                unit = '';
            }
            if (typeof mediumThreshold == 'undefined') {
                mediumThreshold = 0.5;
            }
            if (typeof highThreshold == 'undefined') {
                highThreshold = 0.9;
            }

            if (monitor.monitors[id].chart_config == null) {
                monitor.setup_chart(id);
                return;
            }
            monitor.monitors[id].chart_config.maxValue = max;
            monitor.monitors[id].chart_config.mediumThreshold = mediumThreshold;
            monitor.monitors[id].chart_config.highThreshold = highThreshold;
            monitor.monitors[id].chart_config.displayUnit = unit;
            monitor.monitors[id].chart_render.update(values);
        }
    };
    monitor.confirm_delete_popup = function(id, name) {
        service.update_delete_monitor(id, name);
        $('#delete_monitor_modal').modal('show');
    };
    monitor.delete = function(id) {
        var data = {
            dashboardId: service.get_user_settings().current_dashboard,
            'id': id
        };
        service.delete_monitor(data, function(response) {
            if (response.status >= 200 && response.status < 300) {
                monitor.delete_monitor.error = false;
                monitor.delete_monitor.error_message = '';
                monitor.delete_monitor.success = true;
                monitor.delete_monitor.success_message = "Successfully deleted monitor: " + response.data.data.name;
                monitor.delete_monitor.id = '';
                setTimeout(function() {
                    monitor.delete_monitor.success = false;
                    $('#delete_monitor_modal').modal('hide');
                    service.update_monitor_order();
                }, 3000);
            } else {
                monitor.delete_monitor.error = true;
                monitor.delete_monitor.error_message = response.data.error + ": " + response.data.message;
                monitor.delete_monitor.success = false;
            }
        });
    };
    monitor.cancel_delete_monitor_modal = function() {
        $('#delete_monitor_modal').modal('hide');
    };

    monitor.toggle_monitoring = function() {
        service.toggle_monitoring_status();
    };

    $scope.$on('end-repeat', function() {
        setTimeout(function() {
            $.each(monitor.monitors, function(key, value) {
                if (!value.chart_render) {
                    monitor.setup_chart(key);
                }
            });
        }, 1000);
    });
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
    $scope.$watch(function(scope) { return service.get_reconnect_status(); },
        function(new_val, old_val) { monitor.automatic_reconnect = new_val; }
    );
    $scope.$watch(function(scope) { return service.get_session_status(); },
        function(new_val, old_val) { monitor.session_expired = (new_val == 'expired'); }
    );
    $scope.$watch(function(scope) { return service.get_monitor_marked_for_deletion(); },
        function(new_val, old_val) {
            monitor.delete_monitor.id = new_val;
            if (monitor.monitors[new_val]) {
                monitor.delete_monitor.name = monitor.monitors[new_val].name;
            }
        }
    );
    $scope.$watch(function(scope) { return service.get_monitor_order(); },
        function(new_val, old_val) { monitor.ordered_monitors(); }
    );
});