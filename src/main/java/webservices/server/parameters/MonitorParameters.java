package webservices.server.parameters;

public class MonitorParameters extends webservices.Parameters {
    protected long id;
    protected String name;
    protected long dashboardId;
    protected String url;

    public MonitorParameters() {
        super();
        this.type = Type.SERVER;
    }

    public MonitorParameters(long id, String name, long dashboardId, String statType, String interval, String url) {
        this.id = id;
        this.name = name;
        this.dashboardId = dashboardId;
        this.statType = statType;
        this.interval = interval;
        this.url = url;
        this.type = Type.SERVER;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public long getDashboardId() { return dashboardId; }

    public String getStatType() { return statType; }
    public String getInterval() { return interval; }
    public String getUrl() { return url; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDashboardId(long dashboardId) { this.dashboardId = dashboardId; }

    public void setStatType(String statType) { this.statType = statType; }
    public void setInterval(String interval) { this.interval = interval; }
    public void setUrl(String url) { this.url = url; }
}
