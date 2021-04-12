package com.example.sensors.api.sensor;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PressureSensor extends Sensor {

    private String engineId;
    private String name;

    public PressureSensor(Map<String, Object> sensorMap) {
        super(sensorMap);
        this.engineId = (String) sensorMap.get("engine");
        this.name     = (String) sensorMap.get("name");
    }

    @Override
    public boolean hasError(int refValue) {
        // skip test if reference value is inapplicable
        if (refValue < this.getMinValue() || refValue > this.getMaxValue()) return false;
        return this.getValue() < refValue;
    }

}
