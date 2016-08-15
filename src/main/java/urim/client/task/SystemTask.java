package urim.client.task;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import urim.Message;
import urim.client.mq.Parameters;
import urim.data.SystemData;

public class SystemTask extends Task {
    private static SystemData cachedStat;
    private static long lastUpdatedTimestamp = 0;

    public Message getStats(String key, String statType, long counter) {
        long currentTimestamp = System.currentTimeMillis();
        if (currentTimestamp - 4000 > lastUpdatedTimestamp) {
            lastUpdatedTimestamp = currentTimestamp;
            cachedStat = determineSystemInfo();
        }
        return new Message(counter, cachedStat, name, new Parameters(key, "system", statType));
    }

    private SystemData determineSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        FileSystem fs = systemInfo.getOperatingSystem().getFileSystem();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor cpu = hardware.getProcessor();
        GlobalMemory mem = hardware.getMemory();
        OSFileStore[] filestore = fs.getFileStores();
        NetworkIF[] network = hardware.getNetworkIFs();

        double cpuUsed = cpu.getSystemCpuLoad();
        double cpuTotal = 1;
        long memTotal = mem.getTotal();
        long memUsed = memTotal - mem.getAvailable();
        long fsTotal = 0;
        long fsUsed = 0;
        for (OSFileStore fileSystem : filestore) {
            fsTotal += fileSystem.getTotalSpace();
            fsUsed += fileSystem.getTotalSpace() - fileSystem.getUsableSpace();
        }
        long bytesSent = 0;
        long bytesRecv = 0;
        for (NetworkIF net : network) {
            if (bytesSent < net.getBytesSent()) {
                bytesSent = net.getBytesSent();
            }
            if (bytesRecv < net.getBytesRecv()) {
                bytesRecv = net.getBytesRecv();
            }
        }

        return new SystemData(cpuUsed, cpuTotal, memUsed, memTotal, fsUsed, fsTotal, bytesSent, bytesRecv);
    }
}
