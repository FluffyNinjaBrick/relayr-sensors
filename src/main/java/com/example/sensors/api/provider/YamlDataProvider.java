package com.example.sensors.api.provider;

import com.example.sensors.api.sensor.PressureSensor;
import com.example.sensors.api.sensor.Sensor;
import com.example.sensors.api.sensor.TemperatureSensor;
import com.example.sensors.api.update.UpdateMethod;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Component
public class YamlDataProvider implements DataProvider {

    private final String url = "https://raw.githubusercontent.com/relayr/pdm-test/master/sensors.yml";
    private final String filePath = "./sensors.yml";

    // sensors are mapped by their IDs for ease of referencing
    private final HashMap<String, Sensor> sensors = new HashMap<>();

    @EventListener
    public void getDataOnStartup(ApplicationStartedEvent event) {

        // download file
        System.out.println("Downloading file from URL: " + url + " ...");

        try {
            InputStream in = new URL(url).openConnection().getInputStream();
            Files.copy(in, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("File downloaded");


        // parse YAML
        Yaml yaml = new Yaml();
        System.out.println("Parsing file...");

        try {

            InputStream fileInputStream = new FileInputStream(new File(filePath));
            ArrayList<LinkedHashMap<String, Object>> sensors = yaml.load(fileInputStream);

            // the yaml parser returns a list of hash maps - iterate over it and create sensors
            for (LinkedHashMap<String, Object> sensorMap: sensors) {

                Sensor newSensor = null;   // we'll be adding this

                if (sensorMap.get("type").equals("pressure")) newSensor = new PressureSensor(sensorMap);
                else if (sensorMap.get("type").equals("temperature")) newSensor = new TemperatureSensor(sensorMap);
                else throw new ClassNotFoundException("Error: unrecognized sensor type");

                this.sensors.put(newSensor.getId(), newSensor);    // sensor is no longer null
            }

        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("File parsed, ready");

    }

    @Override
    public List<String> getMalfunctioningEngines(int minPressure, int maxTemperature) {

        // using a hashmap will easily take care of duplicate engine IDs
        HashMap<String, Boolean> brokenEngines = new HashMap<>();

        // check for anomalies
        for (Sensor sensor: this.sensors.values())
            // pressure
            if (sensor instanceof PressureSensor && sensor.getValue() < minPressure)
                brokenEngines.put(((PressureSensor) sensor).getEngineId(), true);
            // temperature
            else if (sensor instanceof TemperatureSensor && sensor.getValue() > maxTemperature) {
                // no check needed for cast, this is guaranteed to work, assuming the config file was correct
                PressureSensor master = (PressureSensor) this.sensors.get(((TemperatureSensor) sensor).getMasterSensorId());
                brokenEngines.put(master.getEngineId(), true);
            }

        // return a list of broken engines
        return new ArrayList<>(brokenEngines.keySet());
    }

    @Override
    public void updateSensor(String id, int value, UpdateMethod method) {

        // edge case - invalid ID
        if (!this.sensors.containsKey(id)) throw new NoSuchElementException("Error: no sensor exists with id " + id);

        Sensor sensor = this.sensors.get(id);
        int newValue;

        // a switch statement would be five times as long, so let's go with ifs
        if (method == UpdateMethod.INCREMENT) newValue = sensor.getValue() + value;
        else if (method == UpdateMethod.DECREMENT) newValue = sensor.getValue() - value;
        else if (method == UpdateMethod.SET) newValue = value;
        else throw new IllegalArgumentException("Error: unrecognized update method: " + method);

        // update if able, notify if unable
        if (newValue >= sensor.getMinValue() && newValue <= sensor.getMaxValue())
            sensor.setValue(newValue);
        else throw new IllegalArgumentException("Error: value " + newValue + " is out of bounds for sensor");

    }
}
