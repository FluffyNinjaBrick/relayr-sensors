package com.example.sensors.api.sensor;

import com.example.sensors.api.update.UpdateMethod;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SensorTest {

    private static Sensor testSensor;

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

        // have to use an actual implementation since the Sensor class is abstract,
        // but we'll limit the tests to the stuff inherited from Sensor
        testSensor = new PressureSensor(sensorMap);
    }

    @Test
    void constructorTest() {
        assertAll(
                () -> assertEquals("123", testSensor.getId()),
                () -> assertEquals(10,testSensor.getValue()),
                () -> assertEquals(-10, testSensor.getMinValue()),
                () -> assertEquals(30, testSensor.getMaxValue())
        );
    }

    @Test
    void updateTest() {

        // the various methods
        testSensor.update(5, UpdateMethod.SET);
        assertEquals(5, testSensor.getValue());

        testSensor.update(10, UpdateMethod.INCREMENT);
        assertEquals(15, testSensor.getValue());

        testSensor.update(5, UpdateMethod.DECREMENT);
        assertEquals(10, testSensor.getValue());


        // value validation
        assertThrows(IllegalArgumentException.class, () -> testSensor.update(-20, UpdateMethod.SET));
        assertThrows(IllegalArgumentException.class, () -> testSensor.update(40, UpdateMethod.SET));
        assertThrows(IllegalArgumentException.class, () -> testSensor.update(25, UpdateMethod.INCREMENT));
        assertThrows(IllegalArgumentException.class, () -> testSensor.update(-25, UpdateMethod.DECREMENT));

    }

}