package webservices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pong {

    private Long id;
    private String content;

    public Pong() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Pong{" +
                "id='" + id + "', " +
                "content='" + content + '\'' +
                '}';
    }
}
