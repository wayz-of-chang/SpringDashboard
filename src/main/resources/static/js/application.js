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
    stompClient.send("/app/stats", {}, JSON.stringify({ 'name': name, 'statType': 'system', 'url': 'localhost:8080', 'interval': '1s' }));
}

function showGreeting(message) {
    var response = $('#response');
    response.append('<p style="word-wrap:break-word;">' + message + '</p>');
}
