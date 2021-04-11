package com.example.sensors.api.provider;

import com.example.sensors.api.sensor.Sensor;
import com.example.sensors.api.update.UpdateMethod;

import java.util.Collection;
import java.util.List;

public interface DataProvider {

    List<String> getMalfunctioningEngines(int minPressure, int maxTemperature);
    void updateSensor(String id, int value, UpdateMethod method);
    Collection<Sensor> getAllSensors();
    Sensor getSensorByID(String id);

}
