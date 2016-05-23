package webservices.client.rest;

public class Parameters extends webservices.client.base.Parameters {
    public Parameters(String name, String statType) {
        this.name = name;
        this.statType = statType;
        this.type = TYPE_REST;
    }
}
