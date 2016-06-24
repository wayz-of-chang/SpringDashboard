package webservices.server.parameters;

public class MonitorParameters extends webservices.Parameters {
    protected long id;
    protected String name;
    protected long dashboardId;
    protected String url;
    protected String protocol;
    protected String monitorType;
    protected String statType;
    protected String parser;
    protected String chart;

    public MonitorParameters() {
        super();
        this.type = Type.SERVER;
    }

    public MonitorParameters(long id, String name, long dashboardId, String url, String protocol, String monitorType, String statType, String parser, String chart, String interval) {
        this.id = id;
        this.name = name;
        this.dashboardId = dashboardId;
        this.url = url;
        this.protocol = protocol;
        this.monitorType = monitorType;
        this.statType = statType;
        this.parser = parser;
        this.chart = chart;
        this.interval = interval;
        this.type = Type.SERVER;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public long getDashboardId() { return dashboardId; }

    public String getUrl() { return url; }
    public String getProtocol() { return protocol; }
    public String getMonitorType() { return monitorType; }
    public String getStatType() { return statType; }
    public String getParser() { return parser; }
    public String getChart() { return chart; }
    public String getInterval() { return interval; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDashboardId(long dashboardId) { this.dashboardId = dashboardId; }

    public void setUrl(String url) { this.url = url; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public void setMonitorType(String monitorType) { this.monitorType = monitorType; }
    public void setStatType(String statType) { this.statType = statType; }
    public void setParser(String parser) { this.parser = parser; }
    public void setChart(String chart) { this.chart = chart; }
    public void setInterval(String interval) { this.interval = interval; }
}
