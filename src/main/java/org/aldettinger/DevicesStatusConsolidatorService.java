package org.aldettinger;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Handler;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 * Compute a status of the factory line consolidating statuses from all devices on that line.
 */
@RegisterForReflection
@ApplicationScoped
@Named("devicesStatusConsolidator")
public class DevicesStatusConsolidatorService {

    private volatile String status = "No information received yet";

    private final Map<Short, Short> deviceStatuses = new HashMap<>();

    /**
     * Complex use case could be managed through custom java code.
     * That is not possible with tools promoting no code approach.
     */
    @Handler
    void registerEvent(DeviceEvent event) {
        if (event.id() == 0 && event.status() == 0) {
            status = "The whole factory line is DOWN";
            deviceStatuses.clear();
        } else {
            deviceStatuses.put(event.id(), event.status());
            boolean anyStatusAboveThreshold = deviceStatuses.values().stream().anyMatch(s -> s >= 9 );

            if (deviceStatuses.entrySet().size() == 5 && anyStatusAboveThreshold) {
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
