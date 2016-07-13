
function barChartDefaultSettings(){
    return {
        minValue: 0, // The gauge minimum value.
        maxValue: 100, // The gauge maximum value.
        circleThickness: 0.05, // The outer circle thickness as a percentage of it's radius.
        circleFillGap: 0.05, // The size of the gap between the outer circle and wave circle as a percentage of the outer circles radius.
        barColor: "#178BCA", // The color of the outer circle.
        transitionTime: 1000, // The amount of time in milliseconds for the wave to rise from 0 to it's final height.
        textVertPosition: .5, // The height at which to display the percentage text withing the wave circle. 0 = bottom, 1 = top.
        textSize: 1, // The relative height of the text to display in the wave circle. 1 = 50%
        valueCountUp: true, // If true, the displayed value counts up from 0 to it's final value upon loading. If false, the final value is displayed.
        displayUnit: "%", // If true, a % symbol is displayed after the value.
        textColor: "#045681", // The color of the value text when the wave does not overlap it.
        waveTextColor: "#A4DBf8" // The color of the value text when the wave overlaps it.
    };
}

function loadBarChart(elementId, values, config) {
    if(config == null) config = barChartDefaultSettings();

    var chart = d3.select("#" + elementId);
    var x_margin = 30;
    var width = parseInt(chart.style("width"));
    var height = parseInt(chart.style("height"));
    var x = d3.scale.ordinal().rangeRoundBands([0, width], .1).domain(values.map(function(value) { return value.key; }));
    var y = d3.scale.linear().range([height, 0]).domain([0, d3.max(values, function(value) { return value.value; })]);
    var xAxis = d3.svg.axis().scale(x).orient("bottom");
    var yAxis = d3.svg.axis().scale(y).orient("left").ticks(10, "%");
    chart.append("g").attr("class", "barChart x axis").attr("transform", "translate(0, " + height + ")").call(d3.svg.axis().scale(x).orient("bottom"));
    chart.append("g").attr("class", "barChart y axis").call(d3.svg.axis().scale(y).orient("left").ticks(10, "%")).append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(config.displayUnit);
    chart.attr("width", width).attr("height", height).append("g").attr("transform", "translate(0, 0)");
    var bars = chart.selectAll(".barChart.bar").data(values).enter().append("g").attr("class", "barChart bar");

    function ChartUpdater(){
        this.update = function(values){
            x.domain(values.map(function(value) { return value.key; }));
            y.domain([0,  d3.max(values, function(value) { return value.value; })]);
            var bars = chart.select(".barChart.bar");;

            bars.selectAll("rect").data(values).enter().append("rect");
            bars.selectAll("rect").data(values).transition().duration(500).attr("x", function(value) { return x(value.key); }).attr("y", function(value) { return y(value.value); }).attr("fill", config.barColor).attr("width", x.rangeBand()).attr("height", function(value) { return height - y(value.value); });

            bars.selectAll("text").data(values).enter().append("text");
            bars.selectAll("text").data(values).transition().duration(500).text(function(value) { return d3.format(",")(value.value)}).attr("x", function(value) { return x(value.key) + x.rangeBand()/2; }).attr("y", function(value) { return y(value.value) - 5; }).attr("text-anchor", "middle");
           /* $.each(values, function(value) {
                var newFinalValue = parseFloat(value).toFixed(2);
                var textRounderUpdater = function(value){ return Math.round(value); };
                if(parseFloat(newFinalValue) != parseFloat(textRounderUpdater(newFinalValue))){
                    textRounderUpdater = function(value){ return parseFloat(value).toFixed(1); };
                }
                if(parseFloat(newFinalValue) != parseFloat(textRounderUpdater(newFinalValue))){
                    textRounderUpdater = function(value){ return parseFloat(value).toFixed(2); };
                }

                var textTween = function(){
                    var i = d3.interpolate(this.textContent, parseFloat(value).toFixed(2));
                    return function(t) { this.textContent = textRounderUpdater(i(t)) + config.displayUnit; }
                };

                text1.transition()
                    .duration(config.transitionTime)
                    .tween("text", textTween);

                //waveGroup.transition()
                //    .duration(config.transitionTime)
                //    .attr('transform','translate('+waveGroupXPosition+','+newHeight+')')
            });*/
        }
    }

    return new ChartUpdater();
}