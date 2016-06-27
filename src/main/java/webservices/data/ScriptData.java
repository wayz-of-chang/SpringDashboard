package webservices.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScriptData {
    private int returnValue;
    private ArrayList<String> output;
    private ArrayList<String> error;

    public ScriptData(int returnValue, ArrayList<String> output, ArrayList<String> error) {
        this.returnValue = returnValue;
        this.output = output;
        this.error = error;
    }

    public int getReturnValue() { return returnValue; }

    public ArrayList<String> getOutput() { return output; }

    public ArrayList<String> getError() { return error; }
}
