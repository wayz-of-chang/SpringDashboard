function datastructureDiagram(elementId) {
    var g = new dagreD3.graphlib.Graph().setGraph({}).setDefaultEdgeLabel(function() { return {}; });

    g.setNode(0, { label: "UserSetting", class: "type-mongo", id: "datastructure_usersetting" });
    g.setNode(1, { label: "User", class: "type-mysql", id: "datastructure_user" });
    g.setNode(2, { label: "Dashboard", class: "type-mysql", id: "datastructure_dashboard" });
    g.setNode(3, { label: "Monitor", class: "type-mysql", id: "datastructure_monitor" });
    g.setNode(4, { label: "MonitorSetting", class: "type-mysql", id: "datastructure_monitorsetting" });

    g.nodes().forEach(function(v) {
        var node = g.node(v);
        node.rx = node.ry = 5;
        console.log(v);
        $('#' + v.id).tooltip('destroy');
        $('#' + v.id).tooltip();
    });

    g.setEdge(1, 0, { label: "one to one", lineInterpolate: 'basis', id: "datastructure_edge_usersetting" });
    g.setEdge(1, 2, { label: "one to many", lineInterpolate: 'basis', id: "datastructure_edge_userdashboard" });
    g.setEdge(2, 3, { label: "one to many", lineInterpolate: 'basis', id: "datastructure_edge_dashboardmonitor" });
    g.setEdge(3, 4, { label: "one to many", lineInterpolate: 'basis', id: "datastructure_edge_monitorsetting" });

    var render = new dagreD3.render();

    var svg = d3.select("#" + elementId);
    var svgGroup = svg.append("g");

    render(svgGroup, g);

    var width = parseInt(svg.style("width"));
    var scaleFactor = 0.8;
    if (width >= 500) {
        scaleFactor = 1;
    }
    if (width >= 600) {
        scaleFactor = 1.25;
    }
    if (width >= 700) {
        scaleFactor = 1.5;
    }
    if (width >= 992) {
        scaleFactor = 2;
    }
    if (width >= 1200) {
        scaleFactor = 2.5;
    }
    var xCenterOffset = (width - (g.graph().width * scaleFactor)) / 2;
    svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)" + "scale(" + scaleFactor + ", " + scaleFactor + ")").attr("class", "datastructure");
    svg.attr("height", (g.graph().height * scaleFactor) + 100);

    return g;
}

