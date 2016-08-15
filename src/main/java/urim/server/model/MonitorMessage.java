package urim.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import urim.Message;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private long monitorId;
    private MonitorSetting.Protocols protocol;
    private Message message;
    private long timestamp;

    private static Date dateUtil = new Date();

    public MonitorMessage() {}

    public MonitorMessage(long monitorId, MonitorSetting.Protocols protocol, Message message) {
        this.monitorId = monitorId;
        this.protocol = protocol;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public MonitorMessage(long monitorId, MonitorSetting.Protocols protocol, Message message, long timestamp) {
        this.monitorId = monitorId;
        this.protocol = protocol;
        this.message = message;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "MonitorMessage[id=%d, protocol=%s, message='%s']", id, protocol, message);
    }

    public String getId() {
        return this.id;
    }

    public long getMonitorId() {
        return this.monitorId;
    }

    public MonitorSetting.Protocols getProtocol() {
        return this.protocol;
    }

    public Message getMessage() { return this.message; }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMonitorId(long monitorId) {
        this.monitorId = monitorId;
    }

    public void setProtocol(MonitorSetting.Protocols protocol) {
        this.protocol = protocol;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
