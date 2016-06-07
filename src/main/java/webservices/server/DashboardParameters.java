package webservices.server;

public class DashboardParameters extends webservices.Parameters {
    protected String name;
    protected long userId;

    public DashboardParameters() {
        super();
        this.type = Type.SERVER;
    }

    public DashboardParameters(String name, long userId) {
        this.name = name;
        this.userId = userId;
        this.type = Type.SERVER;
    }

    public String getName() { return name; }
    public long getUserId() { return userId; }

    public void setName(String name) { this.name = name; }
    public void setUserId(long userId) { this.userId = userId; }
}
