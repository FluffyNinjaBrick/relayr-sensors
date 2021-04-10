package com.example.sensors.api.sensor;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public class PressureSensor extends Sensor {

    private String engineId;
    private String name;

    public PressureSensor(LinkedHashMap<String, Object> sensorMap) {
        super(sensorMap);
        this.engineId = (String) sensorMap.get("engine");
        this.name     = (String) sensorMap.get("name");
    }

}
