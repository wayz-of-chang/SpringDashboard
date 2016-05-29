package webservices.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.NetworkIF;
import oshi.software.os.OSFileStore;

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

    public SystemData(CentralProcessor cpu, GlobalMemory mem, OSFileStore[] fs, NetworkIF[] network) {
        this.cpuUsed = cpu.getSystemCpuLoad();
        this.cpuTotal = 1;
        this.memTotal = mem.getTotal();
        this.memUsed = this.memTotal - mem.getAvailable();
        for (OSFileStore fileSystem : fs) {
            this.fsTotal += fileSystem.getTotalSpace();
            this.fsUsed += fileSystem.getTotalSpace() - fileSystem.getUsableSpace();
        }
        for (NetworkIF net : network) {
            if (this.bytesSent < net.getBytesSent()) {
                this.bytesSent = net.getBytesSent();
            }
            if (this.bytesRecv < net.getBytesRecv()) {
                this.bytesRecv = net.getBytesRecv();
            }
        }

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
