package org.aldettinger;

import org.apache.camel.builder.RouteBuilder;

public class MicroshiftDemoRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        // Consume factory line device events from MQTT broker
        from("paho-mqtt5:device-events")
            // Transform the JSON payload to Java records
            .unmarshal().json(DeviceEvent.class)
            // Forward the event to the service computing the consolidated status of the factory line
            .bean("devicesStatusConsolidator")
            // Get the consolidated status of the factory line and log it to stdout for demo purpose
            .bean("devicesStatusConsolidator", "getStatus")
            .log("Status: ${body}");

        // Expose the consolidated status of the whole factory line as HTTP endpoint
        from("platform-http:///consolidated-status")
            .bean("devicesStatusConsolidator", "getStatus");
    }
}
