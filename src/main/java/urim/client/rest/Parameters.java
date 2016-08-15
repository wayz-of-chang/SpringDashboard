package urim.client.rest;

public class Parameters extends urim.Parameters {
    public Parameters(String name, String statType) {
        this.name = name;
        this.statType = statType;
        this.type = Type.REST;
    }
}
