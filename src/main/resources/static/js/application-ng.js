var app = angular.module('dashboardApp', ['ngAnimate', 'ngAria', 'ngMessages', 'ngMaterial', 'ngDraggable']);
$(function () {

    $('#login_dropdown_button').click(function() {
        setTimeout(function() { $('#login_username_input').focus(); }, 10);
    });
    $('#new_dashboard_modal').on('shown.bs.modal', function () {
        $('#new_dashboard_name').focus();
    })
    $('#edit_dashboard_modal').on('shown.bs.modal', function () {
        $('#edit_dashboard_name').focus();
    })
    $('#user_upload').fileupload({
        dataType: 'json',
        headers: {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')},
        done: function(e, data) {
            console.log(data);
            var service = angular.element(document.body).injector().get('service');
            service.get_current_user(function(response) {
                service.set_user(response.data.data);
                var data = {
                    userId: service.get_user_property('id')
                };
                service.query_dashboards(data, function(response) {});
                return response;
            });
        },
        progressall: function(e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            console.log(progress);
        },
    });

    var document_width;
    var status_interval;

    $(document).ready(function() {
        document_width = $(document).width();
        var dataflow_diagram = dataflowDiagram("chart_dataflow");
        $('.dataflow-description').hide();
        var datastructure_diagram = datastructureDiagram("chart_datastructure");
        loadStatus("chart_example_status", "raw_example_status");
        loadGauge("chart_example_gauge", "raw_example_gauge");
        loadBar("chart_example_bar", "raw_example_bar");
        loadPie("chart_example_pie", "raw_example_pie");
        loadLine("chart_example_line", "raw_example_line");
    });

    $(window).resize( function() {
        if (document_width != $(document).width()) {
            var monitor = angular.element($('#dashboard_content')).scope().monitor;
            $.each(monitor.monitors, function(key, value) {
                monitor.setup_chart(key);
            });
            document_width = $(document).width();
            d3.selectAll("#chart_dataflow > *").remove();
            var dataflow_diagram = dataflowDiagram("chart_dataflow");
            d3.selectAll("#chart_datastructure > *").remove();
            var datastructure_diagram = datastructureDiagram("chart_datastructure");
            loadStatus("chart_example_status", "raw_example_status");
            loadGauge("chart_example_gauge", "raw_example_gauge");
            loadBar("chart_example_bar", "raw_example_bar");
            loadPie("chart_example_pie", "raw_example_pie");
            loadLine("chart_example_line", "raw_example_line");
        }
    });

    var monitoring_state;
    var paused = false;
    $(window).blur(function() {
        if (paused) {
            return false;
        }
        paused = true;
        var service = angular.element(document.body).injector().get('service');
        monitoring_state = service.monitoring;
        if (monitoring_state) {
            service.disconnect_monitors();
        }
    });

    $(window).focus(function() {
        if (!paused) {
            return false;
        }
        paused = false;
        var service = angular.element(document.body).injector().get('service');
        if (monitoring_state) {
            service.connect_monitors();
        }
    });

    var csrf = null;
    var remember_me = null;
    var cookie = $.cookie('csrf');
    if (cookie != null) {
        cookie = JSON.parse(cookie);
    }
    if (cookie != null) {
        csrf = cookie.csrf;
    }
    cookie = $.cookie('remember_me');
    if (cookie != null) {
        cookie = JSON.parse(cookie);
    }
    if (cookie != null) {
        remember_me = cookie.remember_me;
    }
    if (csrf > "" && remember_me) {
        var service = angular.element(document.body).injector().get('service');
        service.get_current_user(function(response) {
            var cookie = JSON.stringify({csrf: response.headers('X-CSRF-TOKEN')});
            $.cookie('csrf', cookie);
            $('meta[name="_csrf"]').attr('content', response.headers('X-CSRF-TOKEN'));
            service.set_user(response.data.data);
            var data = {
                userId: service.get_user_property('id')
            };
            service.query_dashboards(data, function(response) {});
            return response;
        });
    }

});

