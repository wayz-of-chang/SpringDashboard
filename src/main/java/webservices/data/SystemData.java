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

    public SystemData(CentralProcessor cpu, GlobalMemory mem, OSFileStore[] fs, NetworkIF[] network) {
        this.cpuUsed = cpu.getSystemCpuLoad();
        this.cpuTotal = 1;
        this.memTotal = mem.getTotal();
        this.memUsed = this.memTotal - mem.getAvailable();
        for (OSFileStore fileSystem : fs) {
            this.fsTotal += fileSystem.getTotalSpace();
            this.fsUsed += fileSystem.getTotalSpace() - fileSystem.getUsableSpace();
        }

    }

    public double getCpuUsed() { return cpuUsed; }

    public double getCpuTotal() { return cpuTotal; }

    public double getMemUsed() { return memUsed; }

    public double getMemTotal() { return memTotal; }

    public double getfsUsed() { return fsUsed; }

    public double getfsTotal() { return fsTotal; }
}
