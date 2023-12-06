package org.aldettinger;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Handler;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 * Compute a consolidated status of all devices in the factory line.
 */
@RegisterForReflection
@ApplicationScoped
@Named("devicesStatusConsolidator")
public class DevicesStatusConsolidatorService {

    private volatile String status = "No information received yet";

    private Map<Short, Short> deviceStatuses = new HashMap<>();

    @Handler
    void registerEvent(DeviceEvent event) {
        if (event.id() == 0 && event.status() == 0) {
            status = "The whole factory line is DOWN";
            deviceStatuses.clear();
        } else {
            deviceStatuses.put(event.id(), event.status());
            short consolidatedStatus = deviceStatuses.values().stream().reduce((short) 1, (s1, s2) -> (s2 >=9 ? (short)0 : s1) );

            if (deviceStatuses.entrySet().size() == 5 && consolidatedStatus != 0) {
                status = "The whole line is UP";
            } else {
                status = "Some devices on the factory line are not ready";
            }
        }
    }

    public String getStatus() {
        return this.status;
    }
}
