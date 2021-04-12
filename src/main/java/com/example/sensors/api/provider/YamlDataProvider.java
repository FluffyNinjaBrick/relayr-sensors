package com.example.sensors.api.provider;

import com.example.sensors.api.Engine.Engine;
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

    // engines are mapped by the ID of their pressure sensor, for the same reason
    private final HashMap<String, Engine> engines = new HashMap<>();


    // ------------- init -------------

    @EventListener
    public void getDataOnStartup(ApplicationStartedEvent event) {
        downloadFile();
        parseFileAndCreateSensors();
        createEnginesFromSensors();
    }


    // ------------- main API methods -------------

    @Override
    public List<String> getMalfunctioningEngines(int minPressure, int maxTemperature) {
        List<String> brokenEngines = new ArrayList<>();
        for (Engine engine: engines.values())
            if (engine.isBroken(minPressure, maxTemperature))
                brokenEngines.add(engine.getId());
        return brokenEngines;
    }

    @Override
    public void updateSensor(String id, int value, UpdateMethod method) {

        // notify if non-existent ID
        if (!this.sensors.containsKey(id)) throw new NoSuchElementException("Error: no sensor exists with id " + id);

        // else update relevant sensor
        this.sensors.get(id).update(value, method);

    }


    // ------------- the debugging stuff -------------

    @Override
    public Collection<Sensor> getAllSensors() {
        return this.sensors.values();
    }

    @Override
    public Sensor getSensorByID(String id) {
        return this.sensors.get(id);
    }


    // ------------- a few private methods to split the init logic into smaller parts -------------

    private void downloadFile() {

        System.out.println("\nDownloading file from URL: " + url + " ...");

        try {
            InputStream in = new URL(url).openConnection().getInputStream();
            Files.copy(in, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("File downloaded");

    }

    private void parseFileAndCreateSensors() {

        Yaml yaml = new Yaml();
        System.out.println("Parsing file...");

        try {

            // parse yaml file
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

    private void createEnginesFromSensors() {

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
            this.engines.put(sensor.getId(), new Engine(sensor));

        // add temperature sensors to engines
        for (TemperatureSensor sensor: temperatureSensors)
            this.engines.get(sensor.getMasterSensorId()).addTemperatureSensor(sensor);

    }
}
