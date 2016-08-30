var status = 0;
var gauge = 0;
var bar = 0;
var pie = 0;
var line = 0;

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
        values: [
            {
                key: 'aaa',
                value: 0
            },
            {
                key: 'bbbb',
                value: 20
            },
            {
                key: 'cc',
                value: 30
            }
        ],
        max: 100,
        unit: '%',
        mediumThreshold: 0.3,
        highThreshold: 0.9
    },
    {
        values: [
            {
                key: 'aaa',
                value: 10
            },
            {
                key: 'bbbb',
                value: 40
            },
            {
                key: 'cc',
                value: 100
            }
        ],
        max: 100,
        unit: '%',
        mediumThreshold: 0.3,
        highThreshold: 0.9
    },
    {
        values: [
            {
                key: 'aaa',
                value: 95
            },
            {
                key: 'bbbb',
                value: 20
            },
            {
                key: 'cc',
                value: 50
            }
        ],
        max: 100,
        unit: '%',
        mediumThreshold: 0.3,
        highThreshold: 0.9
    },
    {
        values: [
            {
                key: 'aaa',
                value: 0
            },
            {
                key: 'bbbb',
                value: 20
            },
            {
                key: 'cc',
                value: 50
            }
        ],
        max: 100,
        unit: 'MB',
        mediumThreshold: 0.1,
        highThreshold: 0.9
    },
    {
        values: [
            {
                key: 'aaa',
                value: 0
            },
            {
                key: 'bbbb',
                value: 20
            },
            {
                key: 'cc',
                value: 50
            }
        ],
        max: 50,
        unit: 'anything',
        mediumThreshold: 0.3,
        highThreshold: 0.7
    },
    {
        values: [
            {
                key: 'aaa',
                value: 0
            },
            {
                key: 'bbbb',
                value: 20
            },
            {
                key: 'cc',
                value: 50
            }
        ],
        max: 100,
        unit: '%',
        mediumThreshold: 0.8,
        highThreshold: 0.9
    }
];

var pie_states = [
    [
        {
            key: 'aaa',
            value: 0,
            color: '#FF0000'
        },
        {
            key: 'bbbb',
            value: 20,
            color: '#00FF00'
        },
        {
            key: 'cc',
            value: 30,
            color: '#0000FF'
        }
    ],
    [
        {
            key: 'aaa',
            value: 10,
            color: '#FF0000'
        },
        {
            key: 'bbbb',
            value: 40,
            color: '#00FF00'
        },
        {
            key: 'cc',
            value: 100,
            color: '#0000FF'
        }
    ],
    [
        {
            key: 'aaa',
            value: 95,
            color: '#FF0000'
        },
        {
            key: 'bbbb',
            value: 20,
            color: '#00FF00'
        },
        {
            key: 'cc',
            value: 50,
            color: '#0000FF'
        }
    ],
    [
        {
            key: 'aaa',
            value: 0,
            color: '#FF0000'
        },
        {
            key: 'bbbb',
            value: 20,
            color: '#00FF00'
        },
        {
            key: 'cc',
            value: 50,
            color: '#0000FF'
        }
    ],
    [
        {
            key: 'aaa',
            value: 0,
            color: '#FF0000'
        },
        {
            key: 'bbbb',
            value: 20,
            color: '#00FF00'
        },
        {
            key: 'cc',
            value: 50,
            color: '#0000FF'
        }
    ],
    [
        {
            key: 'aaa',
            value: 0,
            color: '#FF0000'
        },
        {
            key: 'bbbb',
            value: 20,
            color: '#00FF00'
        },
        {
            key: 'cc',
            value: 50,
            color: '#0000FF'
        }
    ]
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
    var status = loadStatusIndicator(id, 'n/a');
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
    chart.update(data_point.value);
    if (data_point.status == 'success') {
        $('#' + id).removeClass('medium high').addClass('low');
    }
    if (data_point.status == 'warning') {
        $('#' + id).removeClass('low high').addClass('medium');
    }
    if (data_point.status == 'failure') {
        $('#' + id).removeClass('medium low').addClass('high');
    }
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
    if (typeof unit == 'undefined') {
        unit = '';
    }
    if (typeof mediumThreshold == 'undefined') {
        mediumThreshold = 0.5;
    }
    if (typeof highThreshold == 'undefined') {
        highThreshold = 0.9;
    }
    var percentage = 1.0 * value / max;
    if (percentage <= mediumThreshold) {
        $('#' + id).removeClass('medium high').addClass('low');
    }
    if (percentage > mediumThreshold) {
        $('#' + id).removeClass('low high').addClass('medium');
    }
    if (percentage > highThreshold) {
        $('#' + id).removeClass('medium low').addClass('high');
    }
    if (unit == '%') {
        value = Math.round(100 * percentage);
        max = 100;
    }

    gauge_config.maxValue = max;
    gauge_config.displayUnit = unit;
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
    if (typeof unit == 'undefined') {
        unit = '';
    }
    if (typeof mediumThreshold == 'undefined') {
        mediumThreshold = 0.5;
    }
    if (typeof highThreshold == 'undefined') {
        highThreshold = 0.9;
    }

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
    var existing_chart_values = {};
    $.each(Object.keys(values), function( index, value ) {
        existing_chart_values[value] = false;
    });

    /* Update Line Chart Values */
    var chart_dates = line_config.dates;
    if (chart_dates.length >= line_config.historySize) {
        chart_dates.shift();
    }
    chart_dates.push(x);

    var chart_values = line_config.values;
    $.each(chart_values, function( index, chart_value ) {
        var key = chart_value.key;
        if (values[key] != null) {
            existing_chart_values[key] = true;
            if (values[key].color != null) {
                chart_value.color = values[key].color;
            }
            if (chart_value.values.length >= line_config.historySize) {
                chart_value.values.shift();
            }
            chart_value.values.push({x: x, y: values[key].value});
        } else {
            // Incoming data does not have existing key
            chart_value.values.push({x: x, y: 0});
        }
    });

    $.each(Object.keys(existing_chart_values), function( index, value ) {
        if (!existing_chart_values[value]) {
            var chart_value = {
                key: value,
                values: [{x: x, y: values[value].value}],
                color: values[value].color
            };
            chart_values.push(chart_value);
        }
    });

    var all_values = [].concat.apply([], chart_values.map(function(chart_value) {
        return chart_value.values.map(function(value) {
            return value.y;
        });
    }));

    var min = Math.min.apply(null, all_values);
    var max = Math.max.apply(null, all_values);

    line_config.minValue = min;
    line_config.maxValue = max;
    chart.update(chart_dates, chart_values);
    line = (parseInt(line) + 1) % line_states.length;
}

