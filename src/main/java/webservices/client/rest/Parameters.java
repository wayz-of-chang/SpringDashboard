package webservices.client.rest;

public class Parameters extends webservices.Parameters {
    public Parameters(String name, String statType) {
        this.name = name;
        this.statType = statType;
        this.type = Type.REST;
    }
}
