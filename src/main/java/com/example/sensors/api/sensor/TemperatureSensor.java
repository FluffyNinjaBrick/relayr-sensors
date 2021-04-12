package com.example.sensors.api.sensor;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TemperatureSensor extends Sensor {

    private String masterSensorId;

    public TemperatureSensor(Map<String, Object> sensorMap) {
        super(sensorMap);
        this.masterSensorId = (String) sensorMap.get("master-sensor-id");
    }

    @Override
    public boolean hasError(int refValue) {
        // skip test if reference value is inapplicable
        if (refValue < this.getMinValue() || refValue > this.getMaxValue()) return false;
        return this.getValue() > refValue;
    }

}
