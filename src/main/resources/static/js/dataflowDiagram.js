function dataflowDiagram(elementId) {
    var g = new dagreD3.graphlib.Graph().setGraph({}).setDefaultEdgeLabel(function() { return {}; });

    g.setNode(0, { label: "Browsers", class: "type-multiple", id: "dataflow_browser", title: "The user's web browser.  Multiple per server." });
    g.setNode(1, { label: "Server", class: "type-singular", id: "dataflow_server", title: "The server." });
    g.setNode(2, { label: "Clients", class: "type-multiple", id: "dataflow_client", title: "The client.  Multiple per server" });
    g.setNode(3, { label: "Server Log", class: "type-log", id: "dataflow_serverlog", title: "Contains server-side logging." });
    g.setNode(4, { label: "MySQL", class: "type-store", id: "dataflow_mysql", title: "The data store for most of the application's data." });
    g.setNode(5, { label: "Systems", class: "type-multiple", id: "dataflow_system", title: "The source of the stats being generated." });
    g.setNode(6, { label: "RabbitMQ", class: "type-mq", id: "dataflow_rabbitmq", title: "Message Queue." });
    g.setNode(7, { label: "Client Log", class: "type-log", id: "dataflow_clientlog", title: "Contains client-side logging." });
    g.setNode(8, { label: "MongoDB", class: "type-store", id: "dataflow_mongodb", title: "The data store for storing user settings." });

    g.nodes().forEach(function(v) {
        var node = g.node(v);
        node.rx = node.ry = 5;
        console.log(v);
        $('#' + v.id).tooltip('destroy');
        $('#' + v.id).tooltip();
    });

    g.setEdge(0, 1, { label: "set settings", lineInterpolate: 'basis', id: "dataflow_edge_setsettings" });
    g.setEdge(1, 0, { label: "send stats", lineInterpolate: 'basis', id: "dataflow_edge_sendstats" });
    g.setEdge(1, 2, { label: "REST", lineInterpolate: 'basis', id: "dataflow_edge_screst" });
    g.setEdge(2, 1, { label: "REST", lineInterpolate: 'basis', id: "dataflow_edge_csrest" });
    g.setEdge(1, 3, { class: "dotted", lineInterpolate: 'basis', id: "dataflow_edge_serverlog" });
    g.setEdge(1, 4, { label: "save settings", lineInterpolate: 'basis', id: "dataflow_edge_savesettings" });
    g.setEdge(2, 5, { label: "get stats", lineInterpolate: 'basis', id: "dataflow_edge_getstats" });
    g.setEdge(5, 2, { label: "stats response", lineInterpolate: 'basis', id: "dataflow_edge_statsresponse" });
    g.setEdge(2, 6, { label: "MQ", lineInterpolate: 'basis', id: "dataflow_edge_cmq" });
    g.setEdge(2, 7, { class: "dotted", lineInterpolate: 'basis', id: "dataflow_edge_clientlog" });
    g.setEdge(6, 8, { label: "MQ", lineInterpolate: 'basis', id: "dataflow_edge_mmq" });
    g.setEdge(8, 1, { label: "MQ", lineInterpolate: 'basis', id: "dataflow_edge_smq" });
    g.setEdge(1, 8, { label: "save user settings", lineInterpolate: 'basis', id: "dataflow_edge_usersettings" });

    var render = new dagreD3.render();

    var svg = d3.select("#" + elementId);
    var svgGroup = svg.append("g");

    render(svgGroup, g);

    var width = parseInt(svg.style("width"));
    var scaleFactor = 0.6;
    if (width >= 500) {
        scaleFactor = 0.8;
    }
    if (width >= 600) {
        scaleFactor = 1;
    }
    if (width >= 700) {
        scaleFactor = 1.25;
    }
    if (width >= 992) {
        scaleFactor = 1.5;
    }
    if (width >= 1200) {
        scaleFactor = 2;
    }
    var xCenterOffset = (width - (g.graph().width * scaleFactor)) / 2;
    svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)" + "scale(" + scaleFactor + ", " + scaleFactor + ")").attr("class", "dataflow");
    svg.attr("height", (g.graph().height * scaleFactor) + 100);

    return g;
}

var animating = false;

function animateDataflow(element, type) {
    if (animating) {
        return;
    }
    animating = true;
    $(element).parent().addClass("active");
    var callback = function() {
        $(element).parent().removeClass("active");
        animating = false;
    }
    if (type == 'rest') {
        animateRestDataflow(callback);
    }
    if (type == 'p-rest') {
        animatePRestDataflow(callback);
    }
    if (type == 'mq') {
        animateMqDataflow(callback);
    }
    if (type == 'p-mq') {
        animatePMqDataflow(callback);
    }
    if (type == 'settings') {
        animateSettingsDataflow(callback);
    }
    if (type != 'rest' && type != 'mq' && type != 'settings') {
        callback();
    }
}

function animateRestDataflow(callback) {
    $('.dataflow-description').show().text('Starting off with the user\'s web browser, the user creates a new monitor.');
    $('#dataflow_browser').addClass('active').delay(7000).queue(function(next) {
        $('#dataflow_browser').removeClass('active');
        $('#dataflow_edge_setsettings').addClass('active');
        $('.dataflow-description').text('The user saves the monitor.  The settings are sent back to the server.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_setsettings').removeClass('active');
        $('#dataflow_server').addClass('active');
        $('.dataflow-description').text('The server stores the new settings, and retrieves the saved settings on a periodic basis.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_server').removeClass('active');
        $('#dataflow_edge_screst').addClass('active');
        $('.dataflow-description').text('The server sends REST requests to clients according to the specified settings.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_screst').removeClass('active');
        $('#dataflow_client').addClass('active');
        $('.dataflow-description').text('The client listens for the REST request, and begins processing when it receives one.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_client').removeClass('active');
        $('#dataflow_edge_getstats').addClass('active');
        $('.dataflow-description').text('The client queries the system for stats according to the parameters specified in the REST request.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_getstats').removeClass('active');
        $('#dataflow_system').addClass('active');
        $('.dataflow-description').text('The system works to produce the stats, either by finding system stats, or by running a custom user-defined script.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_system').removeClass('active');
        $('#dataflow_edge_statsresponse').addClass('active');
        $('.dataflow-description').text('The system responds to the client.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_statsresponse').removeClass('active');
        $('#dataflow_client').addClass('active');
        $('.dataflow-description').text('The client receives the stats and composes a message to send back.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_client').removeClass('active');
        $('#dataflow_edge_csrest').addClass('active');
        $('.dataflow-description').text('The client sends a response back to the server.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_csrest').removeClass('active');
        $('#dataflow_server').addClass('active');
        $('.dataflow-description').text('The server receives the response and composes a response to send to the user\'s browser.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_server').removeClass('active');
        $('#dataflow_edge_sendstats').addClass('active');
        $('.dataflow-description').text('The server sends a JSON response to the browser.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_sendstats').removeClass('active');
        $('#dataflow_browser').addClass('active');
        $('.dataflow-description').text('The browser receives the response, parses the stats, and renders the charts accordingly.  The end.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_browser').removeClass('active');
        $('.dataflow-description').text('').hide();
        callback();
        next();
    });
}

function animatePRestDataflow(callback) {
    $('.dataflow-description').text('The server periodically polls clients with REST requests for stats.');
    $('#dataflow_server').addClass('active').delay(7000).queue(function(next) {
        $('#dataflow_server').removeClass('active');
        $('#dataflow_edge_screst').addClass('active');
        $('.dataflow-description').text('The server sends REST requests to clients according to the stored settings.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_screst').removeClass('active');
        $('#dataflow_client').addClass('active');
        $('.dataflow-description').text('The client listens for the REST request, and begins processing when it receives one.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_client').removeClass('active');
        $('#dataflow_edge_getstats').addClass('active');
        $('.dataflow-description').text('The client queries the system for stats according to the parameters specified in the REST request.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_getstats').removeClass('active');
        $('#dataflow_system').addClass('active');
        $('.dataflow-description').text('The system works to produce the stats, either by finding system stats, or by running a custom user-defined script.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_system').removeClass('active');
        $('#dataflow_edge_statsresponse').addClass('active');
        $('.dataflow-description').text('The system responds to the client.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_statsresponse').removeClass('active');
        $('#dataflow_client').addClass('active');
        $('.dataflow-description').text('The client receives the stats and composes a message to send back.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_client').removeClass('active');
        $('#dataflow_edge_csrest').addClass('active');
        $('.dataflow-description').text('The client sends a response back to the server.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_csrest').removeClass('active');
        $('#dataflow_server').addClass('active');
        $('.dataflow-description').text('The server receives the response and composes a response to send to the user\'s browser.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_server').removeClass('active');
        $('#dataflow_edge_sendstats').addClass('active');
        $('.dataflow-description').text('The server sends a JSON response to the browser.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_sendstats').removeClass('active');
        $('#dataflow_browser').addClass('active');
        $('.dataflow-description').text('The browser receives the response, parses the stats, and renders the charts accordingly.  The end.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_browser').removeClass('active');
        $('.dataflow-description').text('').hide();
        callback();
        next();
    });
}

function animateMqDataflow(callback) {
    $('.dataflow-description').show().text('Starting off with the user\'s web browser, the user creates a new monitor.');
    $('#dataflow_browser').addClass('active').delay(7000).queue(function(next) {
        $('#dataflow_browser').removeClass('active');
        $('#dataflow_edge_setsettings').addClass('active');
        $('.dataflow-description').text('The user saves the monitor.  The settings are sent back to the server.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_setsettings').removeClass('active');
        $('#dataflow_server').addClass('active');
        $('.dataflow-description').text('The server stores the new settings, and retrieves the saved settings on a periodic basis.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_server').removeClass('active');
        $('#dataflow_edge_screst').addClass('active');
        $('.dataflow-description').text('The server sends REST requests to clients to start the clients according to the specified settings.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_screst').removeClass('active');
        $('#dataflow_client').addClass('active');
        $('.dataflow-description').text('The client listens for the REST request, and starts an internal process when one is received.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_client').removeClass('active');
        $('#dataflow_edge_getstats').addClass('active');
        $('.dataflow-description').text('The client periodically queries the system for stats.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_getstats').removeClass('active');
        $('#dataflow_system').addClass('active');
        $('.dataflow-description').text('The system works to produce the stats, either by finding system stats, or by running a custom user-defined script.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_system').removeClass('active');
        $('#dataflow_edge_statsresponse').addClass('active');
        $('.dataflow-description').text('The system responds to the client.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_statsresponse').removeClass('active');
        $('#dataflow_client').addClass('active');
        $('.dataflow-description').text('The client receives the stats and composes a message to send to the message queue.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_client').removeClass('active');
        $('#dataflow_edge_cmq').addClass('active');
        $('.dataflow-description').text('The client sends the stats to the message queue.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_cmq').removeClass('active');
        $('#dataflow_rabbitmq').addClass('active');
        $('.dataflow-description').text('The message is stored in the message queue until it is consumed by the server.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_rabbitmq').removeClass('active');
        $('#dataflow_edge_mmq').addClass('active');
        $('.dataflow-description').text('The server consumes the message, storing it in MongoDB.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_mmq').removeClass('active');
        $('#dataflow_mongodb').addClass('active');
        $('.dataflow-description').text('Messages are cached in MongoDB.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_mongodb').removeClass('active');
        $('#dataflow_edge_smq').addClass('active');
        $('.dataflow-description').text('Separately, the server periodically retrieves stored message queue messages.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_smq').removeClass('active');
        $('#dataflow_server').addClass('active');
        $('.dataflow-description').text('The server analyzes the stored messages, and composes a response for the browser.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_server').removeClass('active');
        $('#dataflow_edge_sendstats').addClass('active');
        $('.dataflow-description').text('The server sends a JSON response to the browser.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_sendstats').removeClass('active');
        $('#dataflow_browser').addClass('active');
        $('.dataflow-description').text('The browser receives the response, parses the stats, and renders the charts accordingly.  The end.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_browser').removeClass('active');
        $('.dataflow-description').text('').hide();
        callback();
        next();
    });
}

function animatePMqDataflow(callback) {
    $('.dataflow-description').text('The client periodically polls the system for stats.');
    $('#dataflow_client').addClass('active').delay(7000).queue(function(next) {
        $('#dataflow_client').removeClass('active');
        $('#dataflow_edge_getstats').addClass('active');
        $('.dataflow-description').text('The client queries the system.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_getstats').removeClass('active');
        $('#dataflow_system').addClass('active');
        $('.dataflow-description').text('The system works to produce the stats, either by finding system stats, or by running a custom user-defined script.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_system').removeClass('active');
        $('#dataflow_edge_statsresponse').addClass('active');
        $('.dataflow-description').text('The system responds to the client.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_statsresponse').removeClass('active');
        $('#dataflow_client').addClass('active');
        $('.dataflow-description').text('The client receives the stats and composes a message to send to the message queue.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_client').removeClass('active');
        $('#dataflow_edge_cmq').addClass('active');
        $('.dataflow-description').text('The client sends the stats to the message queue.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_cmq').removeClass('active');
        $('#dataflow_rabbitmq').addClass('active');
        $('.dataflow-description').text('The message is stored in the message queue until it is consumed by the server.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_rabbitmq').removeClass('active');
        $('#dataflow_edge_mmq').addClass('active');
        $('.dataflow-description').text('The server consumes the message, storing it in MongoDB.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_mmq').removeClass('active');
        $('#dataflow_mongodb').addClass('active');
        $('.dataflow-description').text('Messages are cached in MongoDB.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_mongodb').removeClass('active');
        $('#dataflow_edge_smq').addClass('active');
        $('.dataflow-description').text('Separately, the server periodically retrieves stored message queue messages.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_smq').removeClass('active');
        $('#dataflow_server').addClass('active');
        $('.dataflow-description').text('The server analyzes the stored messages, and composes a response for the browser.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_server').removeClass('active');
        $('#dataflow_edge_sendstats').addClass('active');
        $('.dataflow-description').text('The server sends a JSON response to the browser.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_sendstats').removeClass('active');
        $('#dataflow_browser').addClass('active');
        $('.dataflow-description').text('The browser receives the response, parses the stats, and renders the charts accordingly.  The end.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_browser').removeClass('active');
        $('.dataflow-description').text('').hide();
        callback();
        next();
    });
}

function animateSettingsDataflow(callback) {
    $('.dataflow-description').show().text('Starting off with the user\'s web browser, the user saves his/her settings.');
    $('#dataflow_browser').addClass('active').delay(7000).queue(function(next) {
        $('#dataflow_browser').removeClass('active');
        $('#dataflow_edge_setsettings').addClass('active');
        $('.dataflow-description').text('The browser sends a request containing the new settings to the server.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_setsettings').removeClass('active');
        $('#dataflow_server').addClass('active');
        $('.dataflow-description').text('The server receives the request, and processes it accordingly.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_server').removeClass('active');
        $('#dataflow_edge_savesettings').addClass('active');
        $('#dataflow_edge_usersettings').addClass('active');
        $('.dataflow-description').text('The server stores the updated settings in either MySQL or MongoDB.  The end.');
        next();
    }).delay(5000).queue(function(next) {
        $('#dataflow_edge_savesettings').removeClass('active');
        $('#dataflow_edge_usersettings').removeClass('active');
        $('.dataflow-description').text('').hide();
        callback();
        next();
    });
}