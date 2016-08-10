package webservices.client.mq;

public class Parameters extends webservices.Parameters {
    public Parameters(String name, String statType, String server) {
        this.name = name;
        this.statType = statType;
        this.server = server;
        this.type = Type.MQ;
    }
}
