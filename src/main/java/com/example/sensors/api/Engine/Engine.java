package com.example.sensors.api.Engine;

import com.example.sensors.api.sensor.PressureSensor;
import com.example.sensors.api.sensor.TemperatureSensor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Engine {

    private final PressureSensor pressureSensor;
    private final ArrayList<TemperatureSensor> temperatureSensors;

    public Engine(PressureSensor pressureSensor) {
        this.pressureSensor = pressureSensor;
        this.temperatureSensors = new ArrayList<>();
    }

    public boolean isBroken(int minPressure, int maxTemperature) {

        // check pressure, if ok the engine is not broken
        boolean pressureWrong = pressureSensor.hasError(minPressure);
        if (!pressureWrong) return false;

        // if temperature is wrong too, the engine is malfunctioning
        for (TemperatureSensor sensor: temperatureSensors) if (sensor.hasError(maxTemperature)) return true;
        return false;

    }

    public String getId() {
        return this.pressureSensor.getEngineId();
    }

    public void addTemperatureSensor(TemperatureSensor sensor) {
        this.temperatureSensors.add(sensor);
    }
}
