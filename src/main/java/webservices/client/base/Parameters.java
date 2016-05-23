package webservices.client.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameters {
    protected String interval;
    protected String name;
    protected String statType;
    protected String server;
    protected String port;
    protected int type;

    public static final int TYPE_REST = 1;
    public static final int TYPE_MQ = 2;

    public String getInterval() { return interval; }
    public String getName() { return name; }
    public String getStatType() { return statType; }
    public String getServer() { return server; }
    public String getPort() { return port; }
    public String getType() {
        switch (this.type) {
            case TYPE_REST:
                return "REST";
            case TYPE_MQ:
                return "MQ";
            default:
                return "UNKNOWN";
        }
    }
}
