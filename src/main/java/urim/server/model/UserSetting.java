package urim.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSetting {

    @Id
    private long userId;
    private long currentDashboard;
    private Theme theme;
    private HashMap<Long, ArrayList<Long>> monitorOrder;

    public UserSetting() {}

    public UserSetting(long userId) {
        this.userId = userId;
    }

    public UserSetting(long userId, long currentDashboard, Theme theme, HashMap<Long, ArrayList<Long>> monitorOrder) {
        this.userId = userId;
        this.currentDashboard = currentDashboard;
        this.theme = theme;
        this.monitorOrder = monitorOrder;
    }

    public enum Theme {
        ANGULAR, BOOTSTRAP
    }

    @Override
    public String toString() {
        return String.format(
                "UserSetting[userId=%d, current_dashboard='%s']", userId, currentDashboard);
    }

    public long getUserId() {
        return this.userId;
    }

    public long getCurrentDashboard() {
        return this.currentDashboard;
    }

    public Theme getTheme() { return this.theme; }

    public HashMap<Long, ArrayList<Long>> getMonitorOrder() { return this.monitorOrder; }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setCurrentDashboard(long currentDashboard) {
        this.currentDashboard = currentDashboard;
    }

    public void setTheme(Theme theme) { this.theme = theme; }

    public void setMonitorOrder(HashMap<Long, ArrayList<Long>> monitorOrder) {
        this.monitorOrder = monitorOrder;
    }

}
