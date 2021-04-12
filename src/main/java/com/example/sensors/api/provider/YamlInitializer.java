package com.example.sensors.api.provider;

import com.example.sensors.api.engine.Engine;
import com.example.sensors.api.sensor.PressureSensor;
import com.example.sensors.api.sensor.Sensor;
import com.example.sensors.api.sensor.TemperatureSensor;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
public class YamlInitializer implements Initializer {

    @Override
    public Initializer downloadFile(String url, String filePath) {
        System.out.println("\nDownloading file from URL: " + url + " ...");

        try {
            InputStream in = new URL(url).openConnection().getInputStream();
            Files.copy(in, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("File downloaded");
        return this;
    }

    @Override
    public Initializer parseFileAndCreateSensors(String filePath, HashMap<String, Sensor> sensors) {
        Yaml yaml = new Yaml();
        System.out.println("Parsing file...");

        try {

            // parse yaml file
            InputStream fileInputStream = new FileInputStream(new File(filePath));
            ArrayList<LinkedHashMap<String, Object>> sensorMaps = yaml.load(fileInputStream);

            // the yaml parser returns a list of hash maps - iterate over it and create sensors
            for (LinkedHashMap<String, Object> sensorMap: sensorMaps) {

                Sensor newSensor = null;   // we'll be adding this

                if (sensorMap.get("type").equals("pressure")) newSensor = new PressureSensor(sensorMap);
                else if (sensorMap.get("type").equals("temperature")) newSensor = new TemperatureSensor(sensorMap);
                else throw new ClassNotFoundException("Error: unrecognized sensor type");

                sensors.put(newSensor.getId(), newSensor);    // sensor is no longer null
            }

        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("File parsed, ready");
        return this;
    }

    @Override
    public Initializer createEnginesFromSensors(HashMap<String, Sensor> sensors, HashMap<String, Engine> engines) {
        ArrayList<PressureSensor> pressureSensors = new ArrayList<>();
        ArrayList<TemperatureSensor> temperatureSensors = new ArrayList<>();

        // sort the sensors
        for (Sensor sensor: sensors.values()) {
            if (sensor instanceof PressureSensor) pressureSensors.add((PressureSensor) sensor);
            else if (sensor instanceof TemperatureSensor) temperatureSensors.add((TemperatureSensor) sensor);
            // at this point we are guaranteed the sensors are one of these types, so no default branch is needed
        }

        // create engine for each pressure sensor
        for (PressureSensor sensor: pressureSensors)
            engines.put(sensor.getId(), new Engine(sensor));

        // add temperature sensors to engines
        for (TemperatureSensor sensor: temperatureSensors)
            engines.get(sensor.getMasterSensorId()).addTemperatureSensor(sensor);

        return this;
    }

}
