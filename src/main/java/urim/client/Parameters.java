package urim.client;

public class Parameters extends urim.Parameters {
    protected String value;

    public Parameters(String statType, String server, String value) {
        this.statType = statType;
        this.server = server;
        this.value = value;
    }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
