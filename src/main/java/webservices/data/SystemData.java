package webservices.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemData {
    private double cpuUsed;
    private double cpuTotal;
    private long memUsed;
    private long memTotal;
    private long fsUsed;
    private long fsTotal;
    private long bytesSent;
    private long bytesRecv;

    public SystemData(double cpuUsed, double cpuTotal, long memUsed, long memTotal, long fsUsed, long fsTotal, long bytesSent, long bytesRecv) {
        this.cpuUsed = cpuUsed;
        this.cpuTotal = cpuTotal;
        this.memTotal = memTotal;
        this.memUsed = memUsed;
        this.fsTotal = fsTotal;
        this.fsUsed = fsUsed;
        this.bytesSent = bytesSent;
        this.bytesRecv = bytesRecv;
    }

    public double getCpuUsed() { return cpuUsed; }

    public double getCpuTotal() { return cpuTotal; }

    public long getMemUsed() { return memUsed; }

    public long getMemTotal() { return memTotal; }

    public long getFsUsed() { return fsUsed; }

    public long getFsTotal() { return fsTotal; }

    public long getBytesSent() { return bytesSent; }

    public long getBytesRecv() { return bytesRecv; }
}
