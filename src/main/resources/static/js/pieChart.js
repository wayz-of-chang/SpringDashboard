
function pieChartDefaultSettings(){
    return {
        transitionTime: 500, // The amount of time in milliseconds for the wave to rise from 0 to it's final height.
    };
}

function loadPieChart(elementId, values, config) {
    if(config == null) config = pieChartDefaultSettings();

    var chart = d3.select("#" + elementId);
    var width = parseInt(chart.style("width"));
    var height = parseInt(chart.style("height"));
    var radius = parseInt(Math.min(width, height) / 2);
    chart = chart.attr("width", width).attr("height", height).append("g").attr("transform", "translate(" + width/2 + "," + height/2 + ")").attr("class", "pieChart");

    // create function to draw the arcs of the pie slices.
    var arc = d3.svg.arc().outerRadius(radius - 10).innerRadius(0);

    // create a function to compute the pie slice angles.
    var pie = d3.layout.pie().sort(null).value(function(d) { return d.value; });

    // Draw the pie slices.
    var pieChunk = chart.selectAll("g").data(pie(values)).enter().append("g");
    pieChunk.append("path").attr("d", arc)
        .each(function(d) { this._current = d; })
        .style("fill", function(d) { return d.data.color; });
    pieChunk.append("text").attr("text-anchor", "middle");
    pieChunk.append("title").attr("text-anchor", "end");
    chart.selectAll("text").data(pie(values)).text(function(d) { return (d.data.value == 0 ? "" : d.data.key); }).attr("x", function(d) { return 0.5*radius*Math.cos(0.5*(d.startAngle+d.endAngle-Math.PI)); }).attr("y", function(d) { return 0.5*radius*Math.sin(0.5*(d.startAngle+d.endAngle-Math.PI)); }).attr("fill", function(d) {
        return intToRGB(hashCode(d.data.color));
    });

    // Animating the pie-slice requiring a custom function which specifies
    // how the intermediate paths should be drawn.
    function arcTween(a) {
        var i = d3.interpolate(this._current, a);
        this._current = i(0);
        return function(t) { return arc(i(t));    };
    }

    function hashCode(str) {
        if (str.match(/^#[0-9a-fA-F]{6}/) == null) {
            console.log("Invalid color specified for pie chart: " + str + ".  Color should be in HEX (#XXXXXX) format.");
            return 0;
        }
        return parseInt(str.replace(/^#/, ''), 16);
    }

    function intToRGB(i){
        var c = (0xFFFFFF ^ i)
            .toString(16)
            .toUpperCase();

        return "#000000".substring(0, 7 - c.length) + c;
    }

    function ChartUpdater(){
        // create function to update pie-chart. This will be used by histogram.
        this.update = function(values){
            var pieChunk = chart.selectAll("g").data(pie(values)).enter().append("g");
            pieChunk.append("path").attr("d", arc).each(function(d) { this._current = d; }).style("fill", function(d) { return d.data.color; });
            chart.selectAll("path").data(pie(values)).transition().duration(config.transitionTime)
                .attrTween("d", arcTween).style("fill", function(d) { return d.data.color; });
            pieChunk.append("text").attr("text-anchor", "middle");
            pieChunk.append("title").attr("text-anchor", "end");
            chart.selectAll("text").data(pie(values)).transition().duration(config.transitionTime).text(function(d) { return (d.data.value == 0 ? "" : d.data.key); }).attr("x", function(d) { return 0.5*radius*Math.cos(0.5*(d.startAngle+d.endAngle-Math.PI)); }).attr("y", function(d) { return 0.5*radius*Math.sin(0.5*(d.startAngle+d.endAngle-Math.PI)); }).attr("fill", function(d) {
                return intToRGB(hashCode(d.data.color));
            });
            chart.selectAll("title").data(pie(values)).text(function(d) { return "Value: " + d.data.value; });
        }
    }

    return new ChartUpdater();
}