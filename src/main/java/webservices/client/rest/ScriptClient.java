package webservices.client.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webservices.Message;
import webservices.data.ScriptData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class ScriptClient extends Client {

    @RequestMapping("/script")
    public Message script(@RequestParam(value="name", defaultValue="ping.bat") String name) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        int exitValue = -1;
        ArrayList<String> output = new ArrayList<String>();
        ArrayList<String> error = new ArrayList<String>();
        try {
            String prefix = "";
            String osName = System.getProperty("os.name");
            if (osName.equals( "Windows NT" )) {
                prefix = "cmd.exe /C ";
            } else if (osName.equals( "Windows 95" )) {
                prefix = "command.com /C ";
            }
            ProcessBuilder builder = new ProcessBuilder(prefix + getClass().getClassLoader().getResource(String.format("scripts/%s", name)).getPath());
            Process process = builder.start();
            Callable<ArrayList<String>> outputCallable = new StreamGobbler(process.getInputStream());
            Callable<ArrayList<String>> errorCallable = new StreamGobbler(process.getErrorStream());
            Future<ArrayList<String>> outputFuture = pool.submit(outputCallable);
            Future<ArrayList<String>> errorFuture = pool.submit(errorCallable);
            exitValue = process.waitFor();
            output = outputFuture.get();
            error = errorFuture.get();
            outputFuture.cancel(false);
            errorFuture.cancel(false);
            process.destroy();
            pool.shutdown();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return new Message(counter.incrementAndGet(), new ScriptData(exitValue, output, error), name, new Parameters(name, "script"));
    }
}

class StreamGobbler implements Callable<ArrayList<String>> {
    BufferedReader outputBufferedReader;
    ArrayList<String> output;

    StreamGobbler(InputStream processOutputStream) {
        InputStreamReader outputStreamReader = new InputStreamReader(processOutputStream);
        this.outputBufferedReader = new BufferedReader(outputStreamReader);
        this.output = new ArrayList<String>();
    }

    public ArrayList<String> call() {
        try {
            String line;
            while ((line = this.outputBufferedReader.readLine()) != null) {
                this.output.add(line);
            }
            this.outputBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.output;
    }
}