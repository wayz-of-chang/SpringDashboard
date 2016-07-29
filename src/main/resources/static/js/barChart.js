
function barChartDefaultSettings(){
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

function loadBarChart(elementId, values, config) {
    if(config == null) config = barChartDefaultSettings();

    var chart = d3.select("#" + elementId);
    var margin = {top: 20, right: 10, bottom: 30, left: 30};
    var width = parseInt(chart.style("width"));
    var height = parseInt(chart.style("height"));
    chart = chart.append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    var x = d3.scale.ordinal().rangeRoundBands([0, width - margin.left - margin.right], .1).domain(values.map(function(value) { return value.key; }));
    var xAxis = d3.svg.axis().scale(x).orient("bottom");
    chart.append("g").attr("class", "barChart x axis").attr("transform", "translate(0," + (height - margin.top - margin.bottom) + ")").call(d3.svg.axis().scale(x).orient("bottom"));
    var y = d3.scale.linear().range([height - margin.top - margin.bottom, 0]).domain([0, d3.max(values, function(value) { return value.value; })]);
    var yAxis = d3.svg.axis().scale(y).orient("left").ticks(10);
    chart.append("g").attr("class", "barChart y axis").call(d3.svg.axis().scale(y).orient("left").ticks(10, "%")).append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(config.displayUnit);
    var bars = chart.selectAll(".barChart.bar").data(values).enter().append("g").attr("class", "barChart bar");

    function ChartUpdater(){
        this.update = function(values){
            //x.domain(values.map(function(value) { return value.key; }));
            var x = d3.scale.ordinal().rangeRoundBands([0, width - margin.left - margin.right], .1).domain(values.map(function(value) { return value.key; }));
            var xAxis = d3.svg.axis().scale(x).orient("bottom");
            chart.selectAll(".barChart.x.axis").remove();
            chart.append("g").attr("class", "barChart x axis").attr("transform", "translate(0," + (height - margin.top - margin.bottom) + ")").call(xAxis);
            //y.domain([0,  d3.max(values, function(value) { return value.value; })]);
            var y = d3.scale.linear().range([height - margin.top - margin.bottom, 0]).domain([0, config.maxValue]);
            var yAxis = d3.svg.axis().scale(y).orient("left").ticks(10);
            chart.selectAll(".barChart.y.axis").remove();
            chart.append("g").attr("class", "barChart y axis").call(yAxis).append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(config.displayUnit);
            var bars = chart.select(".barChart.bar");;

            if (config.valueCountUp) {
                bars.selectAll("rect").data(values).enter().append("rect").attr("y", function(value) { return height - margin.top - margin.bottom; }).attr("height", function(value) { return 0; });
            } else {
                bars.selectAll("rect").data(values).enter().append("rect");
            }
            bars.selectAll("rect").data(values).transition().duration(config.transitionTime).attr("x", function(value) { return x(value.key); }).attr("y", function(value) { return y(value.value); }).attr("fill", function(value) {
                var color = config.lowBarColor;
                if (value.value > config.mediumThreshold * config.maxValue) {
                    color = config.mediumBarColor;
                }
                if (value.value > config.highThreshold * config.maxValue) {
                    color = config.highBarColor;
                }
                return color;
            }).attr("width", x.rangeBand()).attr("height", function(value) { return height - margin.top - margin.bottom - y(value.value); });

            bars.selectAll("text").data(values).enter().append("text").text(0);
            bars.selectAll("text").data(values).transition().duration(config.transitionTime).tween("text", function(value) {
                var newFinalValue = parseFloat(value.value).toFixed(2);
                var textRounderUpdater = function(value){ return Math.round(value); };
                if(parseFloat(newFinalValue) != parseFloat(textRounderUpdater(newFinalValue))){
                    textRounderUpdater = function(value){ return parseFloat(value).toFixed(1); };
                }
                if(parseFloat(newFinalValue) != parseFloat(textRounderUpdater(newFinalValue))){
                    textRounderUpdater = function(value){ return parseFloat(value).toFixed(2); };
                }

                var i = d3.interpolate(this.textContent, newFinalValue);
                return function(t) { this.textContent = textRounderUpdater(i(t))}
            }).attr("x", function(value) { return x(value.key) + x.rangeBand()/2; }).attr("y", function(value) { return y(value.value) - 5; }).attr("fill", function(value) {
                var color = config.lowTextColor;
                if (value.value > config.mediumThreshold * config.maxValue) {
                    color = config.mediumTextColor;
                }
                if (value.value > config.highThreshold * config.maxValue) {
                    color = config.highTextColor;
                }
                return color;
            }).attr("text-anchor", "middle");
        }
    }

    return new ChartUpdater();
}