package webservices.client.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webservices.Message;
import webservices.data.ScriptData;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class ScriptClient extends Client {

    @RequestMapping("/script")
    public Message script(@RequestParam(value="name", defaultValue="") String name) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        int exitValue = -1;
        ArrayList<String> output = new ArrayList<String>();
        ArrayList<String> error = new ArrayList<String>();
        Process process = null;
        try {
            String prefix = "";
            String osName = System.getProperty("os.name");
            if (osName.equals( "Windows NT" )) {
                prefix = "cmd.exe /C ";
                if (name.equals("")) {
                    name = "ping.bat";
                }
            } else if (osName.equals( "Windows 95" )) {
                prefix = "command.com /C ";
                if (name.equals("")) {
                    name = "ping.bat";
                }
            } else if (osName.contains( "Windows" )) {
                if (name.equals("")) {
                    name = "ping.bat";
                }
            } else if (osName.contains( "Linux" )) {
                if (name.equals("")) {
                    name = "ping.sh";
                }
            } else if (osName.contains( "Mac OS X" )) {
                if (name.equals("")) {
                    name = "ping.sh";
                }
            }
            ProcessBuilder builder;
            try {
                builder = new ProcessBuilder(prefix + getClass().getClassLoader().getResource(String.format("scripts/%s", name)).getPath());
                process = builder.start();
            } catch (IOException e) {
                try {
                    /* The last part of this is really some Java nonsense.  It's this kind of thing that makes me wonder that maybe I chose the wrong technology, and Java should go the way of the dodo. */
                    String parentPath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath().replaceFirst(String.format("^file:\\%s", File.separator), "");
                    parentPath = URLDecoder.decode(parentPath, "UTF-8");
                    if (osName.contains("Linux")) {
                        parentPath = "/" + parentPath;
                        builder = new ProcessBuilder();
                        builder.command("bash", "-c", "\"" + parentPath + File.separator + "scripts" + File.separator + name + "\"");
                    } else {
                        builder = new ProcessBuilder(prefix + "\"" + parentPath + File.separator + "scripts" + File.separator + name + "\"");
                    }
                    process = builder.start();
                } catch (IOException f) {
                    error.add(String.format("Script %s could not be found", name));
                }
            }
            if (process != null) {
                Callable<ArrayList<String>> outputCallable = new StreamGobbler(process.getInputStream());
                Callable<ArrayList<String>> errorCallable = new StreamGobbler(process.getErrorStream());
                Future<ArrayList<String>> outputFuture = pool.submit(outputCallable);
                Future<ArrayList<String>> errorFuture = pool.submit(errorCallable);
                exitValue = process.waitFor();
                output = outputFuture.get();
                error = errorFuture.get();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try { process.getInputStream().close(); } catch (Exception e) {}
            try { process.getOutputStream().close(); } catch (Exception e) {}
            try { process.getErrorStream().close(); } catch (Exception e) {}
            try { process.destroy(); } catch (Exception e) {}
            pool.shutdown();
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
        } catch (IOException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            try { if (this.outputBufferedReader != null) this.outputBufferedReader.close(); } catch (IOException e) {}
        }
        return this.output;
    }
}