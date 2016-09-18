package urim.server.parameters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserParameters extends urim.Parameters {
    protected String username;
    protected String password;
    protected String role;
    protected String email;

    protected long userId;
    protected long currentDashboard;
    protected String theme;
    protected HashMap<Long, ArrayList<Long>> monitorOrder;

    public UserParameters() {
        super();
    }

    public UserParameters(String username, String password, String role, String email, long userId, long
            currentDashboard, String theme, HashMap<Long, ArrayList<Long>> monitorOrder) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.userId = userId;
        this.currentDashboard = currentDashboard;
        this.theme = theme;
        this.monitorOrder = monitorOrder;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public long getUserId() { return userId; }
    public long getCurrentDashboard() { return currentDashboard; }
    public String getTheme() { return theme; }
    public HashMap<Long, ArrayList<Long>> getMonitorOrder() { return monitorOrder; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setEmail(String email) { this.email = email; }
    public void setUserId(long userId) { this.userId = userId; }
    public void setCurrentDashboard(long currentDashboard) { this.currentDashboard = currentDashboard; }
    public void setTheme(String theme) { this.theme = theme; }
    public void setMonitorOrder(HashMap<Long, ArrayList<Long>> monitorOrder) { this.monitorOrder = monitorOrder; }
}
