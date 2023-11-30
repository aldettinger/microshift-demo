package org.aldettinger;

import org.apache.camel.builder.RouteBuilder;

public class DemoRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        //from("paho-mqtt5:queue?brokerUrl=tcp://amq-broker:1883").log("mqtt message received");
        from("paho-mqtt5:queue").log("mqtt message received");
    }
}
