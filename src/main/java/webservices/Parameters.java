package webservices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameters {
    protected String interval;
    protected String name;
    protected String statType;
    protected String server;
    protected String port;
    protected String type;
    public static final String TYPE_REST = "REST";
    public static final String TYPE_MQ = "MQ";
    public static final String TYPE_SERVER = "SERVER";

    public String getInterval() { return interval; }
    public String getName() { return name; }
    public String getStatType() { return statType; }
    public String getServer() { return server; }
    public String getPort() { return port; }
    public String getType() { return type; }
}
