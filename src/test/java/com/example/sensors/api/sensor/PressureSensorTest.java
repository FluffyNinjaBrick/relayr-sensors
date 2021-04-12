package com.example.sensors.api.sensor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PressureSensorTest {

    private static PressureSensor testSensor;

    @BeforeAll
    static void init() {
        Map<String, Object> sensorMap = Map.of(
                "id", "123",
                "value", 10,
                "min_value", -10,
                "max_value", 30,
                "engine", "e123",
                "name", "Engine 123"
        );
        testSensor = new PressureSensor(sensorMap);
    }

    @Test
    void constructorTest() {
        // check the fields not covered by the tests for the abstract Sensor
        assertAll(
                () -> assertEquals("e123", testSensor.getEngineId()),
                () -> assertEquals("Engine 123", testSensor.getName())
        );
    }

    @Test
    void hasErrorTest() {

        assertFalse(testSensor.hasError(5));    // case 0 - the pressure is above the threshold
        assertFalse(testSensor.hasError(-40));  // case 1 - the reference value is too small to be applicable
        assertFalse(testSensor.hasError(70));   // case 2 - the reference value is too big to be applicable

        assertTrue(testSensor.hasError(20));    // case 3 - the pressure is below the threshold

    }
}