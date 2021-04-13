package com.example.sensors.api.provider;

import com.example.sensors.api.argstore.ArgStore;
import com.example.sensors.api.engine.Engine;
import com.example.sensors.api.sensor.Sensor;
import com.example.sensors.api.update.UpdateMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataProvider {

    // initializer
    private final Initializer initializer;

    @Value("${configFile.path}")
    private String filePath;

    // sensors are mapped by their IDs for ease of referencing
    private final HashMap<String, Sensor> sensors = new HashMap<>();

    // engines are mapped by the ID of their pressure sensor, for the same reason
    private final HashMap<String, Engine> engines = new HashMap<>();


    // ------------- constructor -------------
    @Autowired
    public DataProvider(Initializer initializer) {
        this.initializer = initializer;
    }


    // ------------- init -------------
    @EventListener
    public void getDataOnStartup(ApplicationStartedEvent event) {
        this.initializer
            .downloadFile(ArgStore.url, filePath)
            .parseFileAndCreateSensors(filePath, sensors)
            .createEnginesFromSensors(sensors, engines);
    }


    // ------------- main API methods -------------
    public List<String> getMalfunctioningEngines(int minPressure, int maxTemperature) {
        List<String> brokenEngines = new ArrayList<>();
        for (Engine engine: engines.values())
            if (engine.isBroken(minPressure, maxTemperature))
                brokenEngines.add(engine.getId());
        return brokenEngines;
    }

    public void updateSensor(String id, int value, UpdateMethod method) {

        // notify if non-existent ID
        if (!this.sensors.containsKey(id)) throw new NoSuchElementException("Error: no sensor exists with id " + id);

        // else update relevant sensor
        this.sensors.get(id).update(value, method);

    }


    // ------------- debug helper methods -------------
    public Collection<Sensor> getAllSensors() {
        return this.sensors.values();
    }

    public Sensor getSensorByID(String id) {
        return this.sensors.get(id);
    }

}
