package webservices.client.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import webservices.Message;
import webservices.data.SystemData;

@RestController
public class SystemClient extends Client {

    @RequestMapping("/system")
    public Message system() {
        return new Message(counter.incrementAndGet(), determineSystemInfo(), name, new Parameters(name, "system"));
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
