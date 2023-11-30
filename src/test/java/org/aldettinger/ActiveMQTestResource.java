package org.aldettinger;

import java.util.LinkedHashMap;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import com.github.dockerjava.api.model.Ulimit;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class ActiveMQTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String ACTIVEMQ_IMAGE_NAME = "quay.io/artemiscloud/activemq-artemis-broker:1.0.20";
    private static final String ACTIVEMQ_USERNAME = "artemis";
    private static final int MQTT_PORT = 1883;

    private GenericContainer<?> container;
    private String[] modules;

    @Override
    public Map<String, String> start() {
        DockerImageName imageName = DockerImageName.parse(ACTIVEMQ_IMAGE_NAME);
        container = new GenericContainer<>(imageName)
                .withExposedPorts(MQTT_PORT)
                .withLogConsumer(frame -> System.out.print(frame.getUtf8String()))
                .withEnv("AMQ_USER", ACTIVEMQ_USERNAME)
                .withEnv("AMQ_PASSWORD", "artemisP2wd")
                .waitingFor(Wait.forLogMessage(".*AMQ241001.*", 1))
                .withCreateContainerCmdModifier(
                        cmd -> cmd.getHostConfig().withUlimits(new Ulimit[] { new Ulimit("nofile", 2048L, 2048L) }));

        container.start();

        int containerPort = container.getMappedPort(MQTT_PORT);
        String containerHost = container.getHost();

        String brokerUrlTcp = String.format("tcp://%s:%d", containerHost, containerPort);

        Map<String, String> result = new LinkedHashMap<>();

        result.put("camel.component.paho-mqtt5.brokerUrl", brokerUrlTcp);
        //result.put("camel.component.paho-mqtt5.userName", ACTIVEMQ_USERNAME);
        //result.put("camel.component.paho-mqtt5.password", ACTIVEMQ_PASSWORD);

        return result;
    }

    @Override
    public void stop() {
        if (container != null) {
            container.stop();
        }
    }
}
