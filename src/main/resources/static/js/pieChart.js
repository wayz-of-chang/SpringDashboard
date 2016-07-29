
function pieChartDefaultSettings(){
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

function loadPieChart(elementId, values, config) {
    if(config == null) config = pieChartDefaultSettings();

    var chart = d3.select("#" + elementId);
    var margin = {top: 20, right: 10, bottom: 30, left: 30};
    var width = parseInt(chart.style("width"));
    var height = parseInt(chart.style("height"));
    var radius = parseInt(Math.min(width, height) / 2);
    chart = chart.attr("width", width).attr("height", height).append("g").attr("transform", "translate(" + width/2 + "," + height/2 + ")").attr("class", "pieChart");

    // create function to draw the arcs of the pie slices.
    var arc = d3.svg.arc().outerRadius(radius - 10).innerRadius(0);

    // create a function to compute the pie slice angles.
    var pie = d3.layout.pie().sort(null).value(function(d) { return d.value; });

    // Draw the pie slices.
    chart.selectAll("path").data(pie(values)).enter().append("path").attr("d", arc)
        .each(function(d) { this._current = d; })
        .style("fill", function(d) { return d.data.color; });

    // Animating the pie-slice requiring a custom function which specifies
    // how the intermediate paths should be drawn.
    function arcTween(a) {
        var i = d3.interpolate(this._current, a);
        this._current = i(0);
        return function(t) { return arc(i(t));    };
    }

    function hashCode(str) {
        var hash = 0;
        for (var i = 0; i < str.length; i++) {
           hash = str.charCodeAt(i) + ((hash << 5) - hash);
        }
        return hash;
    }

    function intToRGB(i){
        var c = (i & 0x00FFFFFF)
            .toString(16)
            .toUpperCase();

        return "00000".substring(0, 6 - c.length) + c;
    }

    function ChartUpdater(){
        // create function to update pie-chart. This will be used by histogram.
        this.update = function(values){
            chart.selectAll("path").data(pie(values)).enter().append("path").attr("d", arc).each(function(d) { this._current = d; }).style("fill", function(d) { return d.data.color; });
            chart.selectAll("path").data(pie(values)).transition().duration(500)
                .attrTween("d", arcTween).style("fill", function(d) { return d.data.color; });
            chart.selectAll("text").data(pie(values)).enter().append("text");
            chart.selectAll("text").data(pie(values)).transition().duration(500).text(function(d) { return d.data.key; }).attr("x", function(d) { return 0.6*radius*Math.cos(0.5*(d.startAngle+d.endAngle)); }).attr("y", function(d) { return 0.6*radius*Math.sin(0.5*(d.startAngle+d.endAngle)); }).attr("fill", function(d) {
                return "#" + intToRGB(0xFFFFFF ^ hashCode(d.data.color.substring(1)));
            }).attr("text-anchor", "middle");
        }

    }

    return new ChartUpdater();
}