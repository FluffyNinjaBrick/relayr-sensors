package com.example.sensors.api.sensor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureSensorTest {

    private static TemperatureSensor testSensor;

    @BeforeAll
    static void init() {
        Map<String, Object> sensorMap = Map.of(
                "id", "123",
                "value", 10,
                "min_value", -10,
                "max_value", 30,
                "master-sensor-id", "8"
        );
        testSensor = new TemperatureSensor(sensorMap);
    }

    @Test
    void constructorTest() {
        // check the fields not covered by the tests for the abstract Sensor
        assertEquals("8", testSensor.getMasterSensorId());
    }

    @Test
    void hasError() {

        assertFalse(testSensor.hasError(20));   // case 0 - the temperature is below the threshold
        assertFalse(testSensor.hasError(-40));  // case 1 - the reference value is too small to be applicable
        assertFalse(testSensor.hasError(70));   // case 2 - the reference value is too big to be applicable

        assertTrue(testSensor.hasError(5));     // case 3 - the temperature is above the threshold

    }
}