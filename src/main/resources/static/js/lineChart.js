
function lineChartDefaultSettings(){
    return {
        minValue: 0, // The gauge minimum value.
        maxValue: 100, // The gauge maximum value.
        dateFormat: "%Y-%m-%d %X", // Date format used for x axis
        historySize: 50, // Number of data points to keep in history
        transitionTime: 1000, // The amount of time in milliseconds for the wave to rise from 0 to it's final height.
        displayUnit: "", // If true, a % symbol is displayed after the value.
        mediumThreshold: 0.5, // The color of the value text when the wave does not overlap it.
        highThreshold: 0.9 // The color of the value text when the wave overlaps it.
    };
}

function loadLineChart(elementId, values, config) {
    if(config == null) config = lineChartDefaultSettings();

    var chart = d3.select("#" + elementId);
    var margin = {top: 20, right: 30, bottom: 30, left: 30};
    var width = parseInt(chart.style("width"));
    var height = parseInt(chart.style("height"));
    chart = chart.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    chart.append("clipPath").attr("id", "chartBody_" + elementId).append("rect").attr("x", 0).attr("y", 0).attr("width", width - margin.left - margin.right).attr("height", height - margin.top - margin.bottom);
    var x = d3.time.scale().range([0, width - margin.left - margin.right]);
    var xAxis = d3.svg.axis().scale(x).orient("bottom");
    chart.append("g").attr("class", "lineChart x axis").attr("transform", "translate(0," + (height - margin.top - margin.bottom) + ")").call(xAxis);
    var y = d3.scale.linear().range([height - margin.top - margin.bottom, 0]);
    var yAxis = d3.svg.axis().scale(y).orient("left");
    chart.append("g").attr("class", "lineChart y axis").call(yAxis).append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(config.displayUnit);
    //var z = d3.scale.ordinal();
    //var formatDate = d3.time.format(config.dateFormat);
    var line = d3.svg.line().x(function(d) { return x(d.x); }).y(function(d) { return y(d.y); });
    var lines = chart.selectAll(".lineChart.line").data(values).enter().append("g").attr("class", "lineChart line");

    function ChartUpdater(){
        this.update = function(dates, values){
            var x = d3.time.scale().range([0, width - margin.left - margin.right]).domain(d3.extent(dates, function(d) { return d; }));
            var xAxis = d3.svg.axis().scale(x).orient("bottom");
            chart.select(".lineChart.x.axis").transition().duration(config.transitionTime).call(xAxis);

            var y = d3.scale.linear().range([height - margin.top - margin.bottom, 0]).domain([config.minValue, config.maxValue]);
            var yAxis = d3.svg.axis().scale(y).orient("left");
            chart.select(".lineChart.y.axis").transition().duration(config.transitionTime).call(yAxis).select("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(config.displayUnit);

            var line = d3.svg.line().x(function(d) { return x(d.x); }).y(function(d) { return y(d.y); });
            var lines = chart.selectAll(".lineChart.line").data(values).enter().append("g").attr("class", "lineChart line");

            lines.append("path");
            chart.selectAll(".lineChart.line path").data(values).transition().duration(config.transitionTime).attr("d", function(d) { return line(d.values); }).attr("stroke", function(d) { return d.color }).attr("clip-path", "url(#chartBody_" + elementId + ")");

            lines.append("text").text("");
            chart.selectAll(".lineChart.line text").data(values).transition().duration(config.transitionTime).text(function(d) { return d.key }).attr("fill", function(d) { return d.color; }).attr("text-anchor", "end").attr("transform", function(d) { return "translate(" + x(d.values[d.values.length - 1].x) + "," + y(d.values[d.values.length - 1].y) + ")"; }).attr("text-anchor", "start");
        }
    }

    return new ChartUpdater();
}