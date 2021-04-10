package com.example.sensors.api.sensor;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
@Setter
public class TemperatureSensor extends Sensor {

    private String masterSensorId;

    public TemperatureSensor(LinkedHashMap<String, Object> sensorMap) {
        super(sensorMap);
        this.masterSensorId = (String) sensorMap.get("master-sensor-id");
    }

}
