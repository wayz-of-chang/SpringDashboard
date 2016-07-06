package webservices.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSetting {

    @Id
    private long userId;
    private long currentDashboard;
    private HashMap<Long, ArrayList<Long>> monitorOrder;

    public UserSetting() {}

    public UserSetting(long userId) {
        this.userId = userId;
    }

    public UserSetting(long userId, long currentDashboard, HashMap<Long, ArrayList<Long>> monitorOrder) {
        this.userId = userId;
        this.currentDashboard = currentDashboard;
        this.monitorOrder = monitorOrder;
    }

    @Override
    public String toString() {
        return String.format(
                "Dashboard[userId=%d, current_dashboard='%s']", userId, currentDashboard);
    }

    public long getUserId() {
        return this.userId;
    }

    public long getCurrentDashboard() {
        return this.currentDashboard;
    }

    public HashMap<Long, ArrayList<Long>> getMonitorOrder() { return this.monitorOrder; }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setCurrentDashboard(long currentDashboard) {
        this.currentDashboard = currentDashboard;
    }

    public void setMonitorOrder(HashMap<Long, ArrayList<Long>> monitorOrder) {
        this.monitorOrder = monitorOrder;
    }

}
