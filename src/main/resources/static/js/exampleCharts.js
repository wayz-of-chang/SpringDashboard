var status = 0;
var gauge = 0;
var bar = 0;
var pie = 0;
var line = 0;

var status_config;
var gauge_config;
var bar_config;
var pie_config;
var line_config;

var status_states = [
    {
        value: 'success',
        status: 'success'
    },
    {
        value: 'warning',
        status: 'warning'
    },
    {
        value: 'failure',
        status: 'failure'
    },
    {
        value: 'anything',
        status: 'success'
    },
    {
        value: 'anything',
        status: 'warning'
    },
    {
        value: 'anything',
        status: 'failure'
    }
];

var gauge_states = [
    {
        value: 0,
        max: 100,
        unit: '%',
        mediumThreshold: 0.3,
        highThreshold: 0.9
    },
    {
        value: 50,
        max: 100,
        unit: '%',
        mediumThreshold: 0.3,
        highThreshold: 0.9
    },
    {
        value: 100,
        max: 100,
        unit: '%',
        mediumThreshold: 0.3,
        highThreshold: 0.9
    },
    {
        value: 20,
        max: 100,
        unit: 'MB',
        mediumThreshold: 0.1,
        highThreshold: 0.9
    },
    {
        value: 40,
        max: 50,
        unit: 'anything',
        mediumThreshold: 0.3,
        highThreshold: 0.7
    },
    {
        value: 60,
        max: 100,
        unit: '%',
        mediumThreshold: 0.8,
        highThreshold: 0.9
    }
];

var bar_states = [
    {
        values: {
            'aaa': {
                value: 0
            },
            'bbbb': {
                value: 20
            },
            'cc': {
                value: 30
            }
        },
        max: 100,
        unit: '%',
        mediumThreshold: 0.3,
        highThreshold: 0.9
    },
    {
        values: {
            'aaa': {
                value: 10
            },
            'bbbb': {
                value: 40
            },
            'cc': {
                value: 100
            }
        },
        max: 100,
        unit: '%',
        mediumThreshold: 0.3,
        highThreshold: 0.9
    },
    {
        values: {
            'aaa': {
                value: 95
            },
            'bbbb': {
                value: 20
            },
            'cc': {
                value: 50
            }
        },
        max: 100,
        unit: '%',
        mediumThreshold: 0.3,
        highThreshold: 0.9
    },
    {
        values: {
            'aaa': {
                value: 0
            },
            'bbbb': {
                value: 20
            },
            'cc': {
                value: 50
            }
        },
        max: 100,
        unit: 'MB',
        mediumThreshold: 0.1,
        highThreshold: 0.9
    },
    {
        values: {
            'aaa': {
                value: 0
            },
            'bbbb': {
                value: 20
            },
            'cc': {
                value: 50
            }
        },
        max: 50,
        unit: 'anything',
        mediumThreshold: 0.3,
        highThreshold: 0.7
    },
    {
        values: {
            'aaa': {
                value: 0
            },
            'bbbb': {
                value: 20
            },
            'cc': {
                value: 50
            }
        },
        max: 100,
        unit: '%',
        mediumThreshold: 0.8,
        highThreshold: 0.9
    }
];

var pie_states = [
    {
        'aaa': {
            value: 0,
            color: '#FF0000'
        },
        'bbbb': {
            value: 20,
            color: '#00FF00'
        },
        'cc': {
            value: 30,
            color: '#0000FF'
        }
    },
    {
        'aaa': {
            value: 10,
            color: '#FF0000'
        },
        'bbbb': {
            value: 40,
            color: '#00FF00'
        },
        'cc': {
            value: 100,
            color: '#0000FF'
        }
    },
    {
        'aaa': {
            value: 95,
            color: '#FF0000'
        },
        'bbbb': {
            value: 20,
            color: '#00FF00'
        },
        'cc': {
            value: 50,
            color: '#0000FF'
        }
    },
    {
        'aaa': {
            value: 0,
            color: '#FF0000'
        },
        'bbbb': {
            value: 20,
            color: '#00FF00'
        },
        'cc': {
            value: 50,
            color: '#0000FF'
        }
    },
    {
        'aaa': {
            value: 0,
            color: '#FF0000'
        },
        'bbbb': {
            value: 20,
            color: '#00FF00'
        },
        'cc': {
            value: 50,
            color: '#0000FF'
        }
    },
    {
        'aaa': {
            value: 0,
            color: '#FF0000'
        },
        'bbbb': {
            value: 20,
            color: '#00FF00'
        },
        'cc': {
            value: 50,
            color: '#0000FF'
        }
    }
];

var line_states = [
    {
        'aaa': {
            value: 0,
            color: '#FF0000'
        },
        'bbbb': {
            value: 20,
            color: '#00FF00'
        },
        'cc': {
            value: 30,
            color: '#0000FF'
        }
    },
    {
        'aaa': {
            value: 10,
            color: '#FF0000'
        },
        'bbbb': {
            value: 40,
            color: '#00FF00'
        },
        'cc': {
            value: 100,
            color: '#0000FF'
        }
    },
    {
        'aaa': {
            value: 95,
            color: '#FF0000'
        },
        'bbbb': {
            value: 20,
            color: '#00FF00'
        },
        'cc': {
            value: 50,
            color: '#0000FF'
        }
    },
    {
        'aaa': {
            value: 0,
            color: '#FF0000'
        },
        'bbbb': {
            value: 20,
            color: '#00FF00'
        },
        'cc': {
            value: 50,
            color: '#0000FF'
        }
    },
    {
        'aaa': {
            value: 0,
            color: '#FF0000'
        },
        'bbbb': {
            value: 20,
            color: '#00FF00'
        },
        'cc': {
            value: 50,
            color: '#0000FF'
        }
    },
    {
        'aaa': {
            value: 0,
            color: '#FF0000'
        },
        'bbbb': {
            value: 20,
            color: '#00FF00'
        },
        'cc': {
            value: 50,
            color: '#0000FF'
        }
    }
];

var status_interval;
var gauge_interval;
var bar_interval;
var pie_interval;
var line_interval;

function loadStatus(id, raw) {
    clearInterval(status_interval);
    d3.selectAll("#" + id + " > *").remove();
    status_config = {};
    var status = loadStatusIndicator(id, 'n/a', status_config);
    status_interval = startUpdates(function(){ updateStatus(id, status, raw)});
    return status;
}

function loadGauge(id, raw) {
    clearInterval(gauge_interval);
    d3.selectAll("#" + id + " > *").remove();
    gauge_config = liquidFillGaugeDefaultSettings();
    gauge_config.minValue = 0;
    gauge_config.maxValue = 100;
    gauge_config.circleThickness = 0.05;
    gauge_config.textVertPosition = 0.5;
    gauge_config.waveAnimateTime = 1000;
    gauge_config.textSize = 0.8;
    var gauge = loadLiquidFillGauge(id, 0, gauge_config);
    gauge_interval = startUpdates(function(){ updateGauge(id, gauge, raw)});
    return gauge;
}

function loadBar(id, raw) {
    clearInterval(bar_interval);
    d3.selectAll("#" + id + " > *").remove();
    bar_config = barChartDefaultSettings();
    bar_config.minValue = 0;
    bar_config.maxValue = 100;
    var bar = loadBarChart(id, [{key: "n/a", value: 0}], bar_config);
    bar_interval = startUpdates(function(){ updateBar(id, bar, raw)});
    return bar;
}

function loadPie(id, raw) {
    clearInterval(pie_interval);
    d3.selectAll("#" + id + " > *").remove();
    pie_config = pieChartDefaultSettings();
    var pie = loadPieChart(id, [{key: "n/a", color: "#000000", value: 1}], pie_config);
    pie_interval = startUpdates(function(){ updatePie(id, pie, raw)});
    return pie;
}

function loadLine(id, raw) {
    clearInterval(line_interval);
    d3.selectAll("#" + id + " > *").remove();
    line_config = lineChartDefaultSettings();
    line_config.minValue = 0;
    line_config.maxValue = 100;
    line_config.dates = [];
    line_config.values = [];
    line_config.interval = 5000;
    var line = loadLineChart(id, [/*{key: "n/a", color: "#000000", values: []}*/], line_config);
    line_interval = startUpdates(function(){ updateLine(id, line, raw)});
    return line;
}

function startUpdates(callback) {
    return setInterval(function() { callback(); }, 5000);
}

function updateStatus(id, chart, raw) {
    var data_point = status_states[status];
    $('#' + raw).text(JSON.stringify(data_point, null, "  "));

    status_config.status = data_point.status;
    chart.update(data_point.value);
    status = (parseInt(status) + 1) % status_states.length;
}

function updateGauge(id, chart, raw) {
    var data_point = gauge_states[gauge];
    $('#' + raw).text(JSON.stringify(data_point, null, "  "));
    var value = data_point.value;
    var max = data_point.max;
    var unit = data_point.unit;
    var mediumThreshold = data_point.mediumThreshold;
    var highThreshold = data_point.highThreshold;

    gauge_config.maxValue = max;
    gauge_config.displayUnit = unit;
    gauge_config.mediumThreshold = mediumThreshold;
    gauge_config.highThreshold = highThreshold;
    chart.update(value);
    gauge = (parseInt(gauge) + 1) % gauge_states.length;
}

function updateBar(id, chart, raw) {
    var data_point = bar_states[bar];
    $('#' + raw).text(JSON.stringify(data_point, null, "  "));
    var values = data_point.values;
    var max = data_point.max;
    var unit = data_point.unit;
    var mediumThreshold = data_point.mediumThreshold;
    var highThreshold = data_point.highThreshold;

    bar_config.maxValue = max;
    bar_config.mediumThreshold = mediumThreshold;
    bar_config.highThreshold = highThreshold;
    bar_config.displayUnit = unit;
    chart.update(values);
    bar = (parseInt(bar) + 1) % bar_states.length;
}

function updatePie(id, chart, raw) {
    var data_point = pie_states[pie];
    $('#' + raw).text(JSON.stringify(data_point, null, "  "));

    chart.update(data_point);
    pie = (parseInt(pie) + 1) % pie_states.length;
}

function updateLine(id, chart, raw) {
    var data_point = line_states[line];
    $('#' + raw).text(JSON.stringify(data_point, null, "  "));
    var values = data_point;
    var x = new Date();

    chart.update(x, values);
    line = (parseInt(line) + 1) % line_states.length;
}

