package urim.client.rest;

public class Parameters extends urim.client.Parameters {
    public Parameters(String statType, String server, String value) {
        super(statType, server, value);
        this.type = Type.REST;
    }
}
