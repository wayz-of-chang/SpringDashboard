package webservices.server;

public class DashboardParameters extends webservices.Parameters {
    protected long id;
    protected String name;
    protected long userId;

    public DashboardParameters() {
        super();
        this.type = Type.SERVER;
    }

    public DashboardParameters(long id, String name, long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.type = Type.SERVER;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public long getUserId() { return userId; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setUserId(long userId) { this.userId = userId; }
}
