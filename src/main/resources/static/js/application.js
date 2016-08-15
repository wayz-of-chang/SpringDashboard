var stompClient = null;

function setConnected(connected) {
    $('#connect').attr('disabled', connected);
    $('#disconnect').attr('disabled', !connected);
    $('#conversationDiv').css('visibility', connected ? 'visible' : 'hidden');
    $('#response').html('');
}

function connect() {
    var cookie = JSON.parse($.cookie('csrf'));
    var socket = new SockJS('/stats');
    stompClient = Stomp.over(socket);
    stompClient.connect({'X-CSRF-TOKEN': cookie.csrf}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function(greeting) {
            showGreeting(JSON.parse(greeting.body).data);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var name = $('#name').val();
    stompClient.send("/monitoring/stats", {}, JSON.stringify({ 'name': name, 'statType': 'system', 'url': 'localhost:8080', 'interval': '1s' }));
}

function showGreeting(message) {
    var response = $('#response');
    response.append('<p style="word-wrap:break-word;">' + message + '</p>');
}

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
    $('#dashboard_content').sortable({
        containment: '#dashboard_content',
        items: '.sortable',
        handle: '.panel-heading',
        cursor: 'move',
        placeholder: 'col-xs-12 col-sm-6 col-md-4 col-lg-2 panel panel-default droppable monitor',
        update: function(event, ui) {
            angular.element($('#dashboard_modals_container')).scope().dashboard.update_monitor_order();
        }
    });
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

    $(document).ready(function() {
        document_width = $(document).width();
    });

    $(window).resize( function() {
        if (document_width != $(document).width()) {
            var monitor = angular.element($('#dashboard_content')).scope().monitor;
            $.each(monitor.monitors, function(key, value) {
                monitor.setup_chart(key);
            });
            document_width = $(document).width();
        }
    });

    var cookie = JSON.parse($.cookie('csrf'));
    var csrf = cookie.csrf;
    cookie = JSON.parse($.cookie('remember_me'));
    var remember_me;
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

