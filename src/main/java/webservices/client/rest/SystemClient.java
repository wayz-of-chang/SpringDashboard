package webservices.client.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OSFileStore;
import webservices.Message;
import webservices.data.SystemData;

@RestController
public class SystemClient extends Client {

    @RequestMapping("/system")
    public Message system() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor cpu = hardware.getProcessor();
        GlobalMemory mem = hardware.getMemory();
        OSFileStore[] fs = hardware.getFileStores();
        NetworkIF[] network = hardware.getNetworkIFs();

        return new Message(counter.incrementAndGet(), new SystemData(cpu, mem, fs, network), name, new Parameters(name, "system"));
    }
}
