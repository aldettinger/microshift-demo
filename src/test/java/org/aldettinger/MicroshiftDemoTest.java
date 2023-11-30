package org.aldettinger;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(ActiveMQTestResource.class)
public class MicroshiftDemoTest {
    
    @Test
    void mqttDoesWhat() throws InterruptedException {
        Thread.sleep(10000L);
    }

}
