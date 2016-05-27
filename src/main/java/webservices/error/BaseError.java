package webservices.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseError {
    protected String errorMessage;
    public BaseError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
