package urim.client.task;

public class Task {
    static final String template = "%s";

    public String getStats(String value) {
        return String.format(template, value);
    }
}
