package com.example.sensors.api.provider;

import com.example.sensors.api.engine.Engine;
import com.example.sensors.api.sensor.Sensor;

import java.util.HashMap;

public interface Initializer {

    Initializer downloadFile(String url, String filePath);
    Initializer parseFileAndCreateSensors(String filePath, HashMap<String, Sensor> sensors);
    Initializer createEnginesFromSensors(HashMap<String, Sensor> sensors, HashMap<String, Engine> engines);

}
