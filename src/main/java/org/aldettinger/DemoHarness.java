package org.aldettinger;

import org.apache.camel.builder.RouteBuilder;

public class DemoHarness extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // TODO: lazyStartProducer really needed ?
        //from("timer:test").to("paho-mqtt5:queue?lazyStartProducer=true&brokerUrl=tcp://amq-broker:1883");
        from("timer:test").setBody(constant("dummy")).to("paho-mqtt5:queue?lazyStartProducer=true");
    }
}
