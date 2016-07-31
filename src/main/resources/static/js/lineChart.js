
function lineChartDefaultSettings(){
    return {
        minValue: 0, // The gauge minimum value.
        maxValue: 100, // The gauge maximum value.
        lowBarColor: "#77DD77", // The color of the bar.
        mediumBarColor: "#DDDD77", // The color of the bar.
        highBarColor: "#FF7777", // The color of the bar.
        lowTextColor: "#44DD44", // The color of the text.
        mediumTextColor: "#DDDD44", // The color of the text.
        highTextColor: "#FF4444", // The color of the text.
        transitionTime: 1000, // The amount of time in milliseconds for the wave to rise from 0 to it's final height.
        valueCountUp: true, // If true, the displayed value counts up from 0 to it's final value upon loading. If false, the final value is displayed.
        displayUnit: "%", // If true, a % symbol is displayed after the value.
        mediumThreshold: 0.5, // The color of the value text when the wave does not overlap it.
        highThreshold: 0.9 // The color of the value text when the wave overlaps it.
    };
}

function loadLineChart(elementId, values, config) {
    if(config == null) config = lineChartDefaultSettings();

    var chart = d3.select("#" + elementId);
    var margin = {top: 20, right: 10, bottom: 30, left: 30};
    var width = parseInt(chart.style("width"));
    var height = parseInt(chart.style("height"));
    chart = chart.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    var x = d3.time.scale().range([0, width - margin.left - margin.right]);
    var xAxis = d3.svg.axis().scale(x).orient("bottom");
    chart.append("g").attr("class", "lineChart x axis").attr("transform", "translate(0," + (height - margin.top - margin.bottom) + ")").call(xAxis);
    var y = d3.scale.linear().range([height - margin.top - margin.bottom, 0]);
    var yAxis = d3.svg.axis().scale(y).orient("left");
    chart.append("g").attr("class", "lineChart y axis").call(yAxis).append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(config.displayUnit);
    var line = d3.line().curve(d3.curveBasis).x(function(d) { return x(d.x); }).y(function(d) { return y(d.y); });
    var lines = chart.selectAll(".lineChart.line").data(values).enter().append("g").attr("class", "lineChart line");

    function ChartUpdater(){
        this.update = function(values){
            //x.domain(values.map(function(value) { return value.key; }));
            var x = d3.time.scale().range([0, width - margin.left - margin.right], .1).domain([0, 99]);
            var xAxis = d3.svg.axis().scale(x).orient("bottom");
            chart.selectAll(".lineChart.x.axis").remove();
            chart.append("g").attr("class", "lineChart x axis").attr("transform", "translate(0," + (height - margin.top - margin.bottom) + ")").call(xAxis);
            //y.domain([0,  d3.max(values, function(value) { return value.value; })]);
            var y = d3.scale.linear().range([height - margin.top - margin.bottom, 0]).domain([0, config.maxValue]);
            var yAxis = d3.svg.axis().scale(y).orient("left");
            chart.selectAll(".lineChart.y.axis").remove();
            chart.append("g").attr("class", "lineChart y axis").call(yAxis).append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(config.displayUnit);
            var lines = chart.select(".lineChart.line");;

            if (config.valueCountUp) {
                bars.selectAll("rect").data(values).enter().append("rect").attr("y", function(value) { return height - margin.top - margin.bottom; }).attr("height", function(value) { return 0; });
            } else {
                lines.selectAll("path").data(values).enter().append("path");
            }
            lines.selectAll("path").data(values).transition().duration(config.transitionTime).attr("d", function(d) { return line(d.values); }).attr("stroke", function(d) { return d.color });

            lines.selectAll("text").data(values).enter().append("text").text("");
            lines.selectAll("text").data(values).transition().duration(config.transitionTime).text(function(d) { return d.key }).attr("fill", function(d) { return d.color; }).attr("text-anchor", "end");
        }
    }

    return new ChartUpdater();
}