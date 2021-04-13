package com.example.sensors.api.provider;

import com.example.sensors.api.engine.Engine;
import com.example.sensors.api.sensor.PressureSensor;
import com.example.sensors.api.sensor.Sensor;
import com.example.sensors.api.sensor.TemperatureSensor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class YamlInitializerTest {

    private static YamlInitializer initializer;
    private static File testConfigFile;
    private final static String configFileName = "testConfig";
    private final static String configFileSuffix = ".yml";

    public final static String fileContent =
            "- id: \"3142\"\n" +
            "  engine: \"123\"\n" +
            "  type: \"pressure\"\n" +
            "  name: \"Engine 123\"\n" +
            "  value: 70\n" +
            "  min_value: 0\n" +
            "  max_value: 120\n" +
            "- id: \"32234\"\n" +
            "  master-sensor-id: \"3142\"\n" +
            "  type: \"temperature\"\n" +
            "  value: 82\n" +
            "  min_value: -50\n" +
            "  max_value: 150\n" +
            "- id: \"57230\"\n" +
            "  master-sensor-id: \"3142\"\n" +
            "  type: \"temperature\"\n" +
            "  value: 101\n" +
            "  min_value: 0\n" +
            "  max_value: 273\n" +
            "- id: \"5703\"\n" +
            "  engine: \"156\"\n" +
            "  location: \"\"\n" +
            "  type: \"pressure\"\n" +
            "  name: \"Engine 156\"\n" +
            "  value: 73\n" +
            "  min_value: 0\n" +
            "  max_value: 150\n" +
            "- id: \"89145\"\n" +
            "  master-sensor-id: \"5703\"\n" +
            "  type: \"temperature\"\n" +
            "  value: 99\n" +
            "  min_value: 0\n" +
            "  max_value: 100\n";

    /*
        We can't really test the download, as that would require an always available, unchanging file to download
        from somewhere. Therefore, we'll simply create a configuration file ourselves, and then remove it once
        testing is done.
     */
    @BeforeAll
    public static void createFile() throws IOException {
        initializer = new YamlInitializer();
        testConfigFile = File.createTempFile(configFileName, configFileSuffix);
        System.out.println("Created temp config file at " + testConfigFile.getAbsolutePath());
        testConfigFile.deleteOnExit();
        FileWriter writer = new FileWriter(testConfigFile);
        writer.write(fileContent);
        writer.close();
    }

    @Test
    void parseFileAndCreateSensorsTest() {

        HashMap<String, Sensor> sensors = new HashMap<>();

        initializer.parseFileAndCreateSensors(testConfigFile.getPath(), sensors);

        assertEquals(5, sensors.size());
        assertTrue(sensors.keySet().containsAll(Arrays.asList("3142", "32234", "57230", "5703", "89145")));

        Sensor sensor1 = sensors.get("3142");
        assertTrue(sensor1 instanceof PressureSensor);
        assertAll(
                () -> assertEquals("123", ((PressureSensor) sensor1).getEngineId()),
                () -> assertEquals("Engine 123", ((PressureSensor) sensor1).getName()),
                () -> assertEquals(70, sensor1.getValue()),
                () -> assertEquals(0, sensor1.getMinValue()),
                () -> assertEquals(120, sensor1.getMaxValue())
        );

        Sensor sensor2 = sensors.get("32234");
        assertTrue(sensor2 instanceof TemperatureSensor);
        assertAll(
                () -> assertEquals("3142", ((TemperatureSensor) sensor2).getMasterSensorId()),
                () -> assertEquals(82, sensor2.getValue()),
                () -> assertEquals(-50, sensor2.getMinValue()),
                () -> assertEquals(150, sensor2.getMaxValue())
        );

        Sensor sensor3 = sensors.get("57230");
        assertTrue(sensor3 instanceof TemperatureSensor);
        assertAll(
                () -> assertEquals("3142", ((TemperatureSensor) sensor3).getMasterSensorId()),
                () -> assertEquals(101, sensor3.getValue()),
                () -> assertEquals(0, sensor3.getMinValue()),
                () -> assertEquals(273, sensor3.getMaxValue())
        );

        Sensor sensor4 = sensors.get("5703");
        assertTrue(sensor4 instanceof PressureSensor);
        assertAll(
                () -> assertEquals("156", ((PressureSensor) sensor4).getEngineId()),
                () -> assertEquals("Engine 156", ((PressureSensor) sensor4).getName()),
                () -> assertEquals(73, sensor4.getValue()),
                () -> assertEquals(0, sensor4.getMinValue()),
                () -> assertEquals(150, sensor4.getMaxValue())
        );

        Sensor sensor5 = sensors.get("89145");
        assertTrue(sensor5 instanceof TemperatureSensor);
        assertAll(
                () -> assertEquals("5703", ((TemperatureSensor) sensor5).getMasterSensorId()),
                () -> assertEquals(99, sensor5.getValue()),
                () -> assertEquals(0, sensor5.getMinValue()),
                () -> assertEquals(100, sensor5.getMaxValue())
        );

    }

    // note - this test depends on the previous one passing, as it is covers the continuation of the processing chain
    @Test
    void createEnginesFromSensorsTest() {

        HashMap<String, Sensor> sensors = new HashMap<>();
        HashMap<String, Engine> engines = new HashMap<>();

        initializer.parseFileAndCreateSensors(testConfigFile.getPath(), sensors);
        initializer.createEnginesFromSensors(sensors, engines);

        assertEquals(2, engines.size());

        Engine engine1 = engines.get("3142");
        assertAll(
                () -> assertEquals(sensors.get("3142"), engine1.getPressureSensor()),
                () -> assertEquals(2, engine1.getTemperatureSensors().size()),
                () -> assertTrue(engine1.getTemperatureSensors().containsAll(Arrays.asList(
                        (TemperatureSensor) sensors.get("32234"),
                        (TemperatureSensor) sensors.get("57230")
                )))
        );

        Engine engine2 = engines.get("5703");
        assertAll(
                () -> assertEquals(sensors.get("5703"), engine2.getPressureSensor()),
                () -> assertEquals(1, engine2.getTemperatureSensors().size()),
                () -> assertTrue(engine2.getTemperatureSensors().contains(
                        sensors.get("89145")    // warning irrelevant
                ))
        );

    }

}