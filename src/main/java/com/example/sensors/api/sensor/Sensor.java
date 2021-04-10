package com.example.sensors.api.sensor;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public abstract class Sensor {

    private String id;
    private int value;
    private int minValue;
    private int maxValue;

    protected Sensor(LinkedHashMap<String, Object> sensorMap) {
        this.id = (String) sensorMap.get("id");
        this.value    = (int) sensorMap.get("value");
        this.minValue = (int) sensorMap.get("min_value");
        this.maxValue = (int) sensorMap.get("max_value");
    }

}

