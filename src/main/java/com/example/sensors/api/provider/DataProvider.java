package com.example.sensors.api.provider;

import com.example.sensors.api.update.UpdateMethod;

import java.util.List;

public interface DataProvider {

    List<String> getMalfunctioningEngines(int minPressure, int maxTemperature);
    void updateSensor(String id, int value, UpdateMethod method);

}
