
function loadStatusIndicator(elementId, value) {
    var status = d3.select("#" + elementId);
    var margin = {top: 20, right: 20, bottom: 20, left: 20};
    var width = parseInt(status.style("width"));
    var height = parseInt(status.style("height"));
    status = status.attr("class", "status").attr("width", width).attr("height", height).append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    var bars = status.selectAll(".status-indicator").data(value).enter().append("g").attr("class", "status-indicator");

    function StatusUpdater(){
        this.update = function(value){
            var bars = status.select(".status-indicator");;

            bars.selectAll("rect").data(value).enter().append("rect").attr("class", "status-box");
            bars.selectAll("rect").data(value).transition().duration(1000).attr("x", 0).attr("y", 0).attr("width", width - margin.left - margin.right).attr("height", function(value) { return height - margin.top - margin.bottom; });

            bars.selectAll("text").data(value).enter().append("text").attr("class", "status-label");
            bars.selectAll("text").data(value).text(value).attr("x", (width - margin.left - margin.right)/2).attr("y", (height - margin.top - margin.bottom) / 1.75).attr("font-size", (Math.round(height - margin.top - margin.bottom) / 3) + "px").attr("text-anchor", "middle");
        }
    }

    return new StatusUpdater();
}