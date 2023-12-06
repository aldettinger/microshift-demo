package org.aldettinger;

import java.util.Random;

import org.apache.camel.builder.RouteBuilder;

public class MicroshiftDemoHarness extends RouteBuilder {

    private static final String DEVICE_EVENT_FORMAT = "{\"id\": %d, \"status\": %d}";

    private String createEvent(Random random) {
        return String.format(DEVICE_EVENT_FORMAT, random.nextInt(5), random.nextInt(10));
    }

    @Override
    public void configure() throws Exception {
        Random random = new Random();

        from("timer:generate-device-events?period={{timer.period}}")
                .process(e -> e.getMessage().setBody(createEvent(random)))
                .to("paho-mqtt5:device-events");
    }
}
