package webservices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import webservices.error.BaseError;

import java.util.Calendar;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    private long id;
    private Object data;
    private String status;
    private String source;
    private Date date;
    private Parameters parameters;

    //Default constructor is required for mapping JSON output to object
    public Message() {}

    public Message(long id, Object data, String source, Parameters parameters) {
        this.id = id;
        this.data = data;
        this.status = "success";
        this.source = source;
        this.parameters = parameters;
        this.date = Calendar.getInstance().getTime();
    }

    public Message(long id, BaseError error, String source, Parameters parameters) {
        this.id = id;
        this.data = error;
        this.status = "failure";
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

    public void setId(long id) { this.id = id; }

    public void setData(Object data) { this.data = data; }

    public void setSource(String source) { this.source = source; }

    public void setParameters(Parameters parameters) { this.parameters = parameters; }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", data='" + data.toString() + '\'' +
                '}';
    }
}
