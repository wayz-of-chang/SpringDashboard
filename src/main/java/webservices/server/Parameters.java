package webservices.server;

public class Parameters extends webservices.Parameters {
    protected String url;

    public Parameters() {
        super();
        this.type = TYPE_SERVER;
    }

    public Parameters(String name, String statType, String interval, String url) {
        this.name = name;
        this.statType = statType;
        this.interval = interval;
        this.url = url;
        this.type = TYPE_SERVER;
    }

    public String getUrl() { return url; }

    public void setName(String name) { this.name = name; }
    public void setStatType(String statType) { this.statType = statType; }
    public void setInterval(String interval) { this.interval = interval; }
    public void setUrl(String url) { this.url = url; }
}
