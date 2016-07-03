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
            angular.element($('#dashboard_modals_container')).scope().dashboard.update_monitor_order(
                $('#dashboard_content').sortable('toArray')
            );
        }
    });
});

