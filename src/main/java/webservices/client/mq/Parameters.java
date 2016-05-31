package webservices.client.mq;

public class Parameters extends webservices.Parameters {
    public Parameters(String interval, String name, String statType, String server, String port) {
        this.interval = interval;
        this.name = name;
        this.statType = statType;
        this.server = server;
        this.port = port;
        this.type = Type.MQ;
    }
}
