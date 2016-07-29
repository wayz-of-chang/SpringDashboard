
function loadStatusIndicator(elementId, value) {
    var status = d3.select("#" + elementId);
    var margin = {top: 20, right: 20, bottom: 20, left: 20};
    var width = parseInt(status.style("width"));
    var height = parseInt(status.style("height"));
    status = status.attr("class", "status").append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    var bars = status.append("g").attr("class", "status-indicator");
    bars.append("rect").attr("class", "status-box").attr("x", 0).attr("y", 0).attr("rx", 20).attr("ry", 20).attr("width", width - margin.left - margin.right).attr("height", function(value) { return height - margin.top - margin.bottom; });
    bars.append("text").attr("class", "status-label").attr("x", (width - margin.left - margin.right)/2).attr("y", (height - margin.top - margin.bottom) / 1.75).attr("font-size", (Math.round(height - margin.top - margin.bottom) / 3) + "px").attr("text-anchor", "middle").text(value);

    function StatusUpdater(){
        this.update = function(value){
            status.selectAll("text").text(value);
        }
    }

    return new StatusUpdater();
}