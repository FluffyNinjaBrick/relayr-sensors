package com.example.sensors.api.engine;

import com.example.sensors.api.sensor.PressureSensor;
import com.example.sensors.api.sensor.TemperatureSensor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

    private static Engine testEngine;

    @BeforeAll
    static void init() {

        // create pressure sensor
        Map<String, Object> pressureSensorMap = Map.of(
                "id", "123",
                "value", 10,
                "min_value", -10,
                "max_value", 30,
                "engine", "e123",
                "name", "Engine 123"
        );
        PressureSensor pressureSensor = new PressureSensor(pressureSensorMap);

        // create two temperature sensors
        Map<String, Object> temperatureSensor1Map = Map.of(
                "id", "124",
                "value", 10,
                "min_value", -10,
                "max_value", 30,
                "master-sensor-id", "123"
        );
        TemperatureSensor temperatureSensor1 = new TemperatureSensor(temperatureSensor1Map);

        Map<String, Object> temperatureSensor2Map = Map.of(
                "id", "125",
                "value", 20,
                "min_value", -10,
                "max_value", 30,
                "master-sensor-id", "123"
        );
        TemperatureSensor temperatureSensor2 = new TemperatureSensor(temperatureSensor2Map);

        // create engine
        testEngine = new Engine(pressureSensor);
        testEngine.addTemperatureSensor(temperatureSensor1);
        testEngine.addTemperatureSensor(temperatureSensor2);
    }

    @Test
    void isBrokenTest() {

        assertFalse(testEngine.isBroken(0, 25));    // case 0 - no sensors are broken
        assertFalse(testEngine.isBroken(15, 25));   // case 1 - just the pressure is wrong
        assertFalse(testEngine.isBroken(0, 15));    // case 2 - just the temperature is wrong

        assertTrue(testEngine.isBroken(15, 15));    // case 3 - both parameters are wrong

    }

    @Test
    void getIdTest() {
        assertEquals("e123", testEngine.getId());
    }
}