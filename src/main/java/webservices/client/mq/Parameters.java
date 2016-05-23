package webservices.client.mq;

public class Parameters extends webservices.client.base.Parameters {
    public Parameters(String interval, String name, String statType, String server, String port) {
        this.interval = interval;
        this.name = name;
        this.statType = statType;
        this.server = server;
        this.port = port;
        this.type = TYPE_MQ;
    }
}
