package webservices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import webservices.client.base.Parameters;

import java.util.Calendar;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    private final long id;
    private Object data;
    private String status;
    private String source;
    private Date date;
    private Parameters parameters;

    public Message(long id, Object data, String source, Parameters parameters) {
        this.id = id;
        this.data = data;
        this.status = "success";
        this.source = source;
        this.parameters = parameters;
        this.date = Calendar.getInstance().getTime();
    }

    public long getId() {
        return id;
    }

    public Object getData() {
        return data;
    }

    public String getSource() { return source; }

    public Parameters getParameters() { return parameters; }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", data='" + data.toString() + '\'' +
                '}';
    }
}
