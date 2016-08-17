package urim.client.mq;

public class Parameters extends urim.client.Parameters {
    public Parameters(String name, String statType, String server, String value) {
        super(statType, server, value);
        this.name = name;
        this.type = Type.MQ;
    }
}
