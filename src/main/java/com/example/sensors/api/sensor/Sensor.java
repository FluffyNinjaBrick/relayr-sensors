package com.example.sensors.api.sensor;

import com.example.sensors.api.update.UpdateMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class Sensor {

    private String id;
    private int value;
    private int minValue;
    private int maxValue;

    protected Sensor(Map<String, Object> sensorMap) {
        this.id = (String) sensorMap.get("id");
        this.value    = (int) sensorMap.get("value");
        this.minValue = (int) sensorMap.get("min_value");
        this.maxValue = (int) sensorMap.get("max_value");
    }


    public void update(int value, UpdateMethod method) {

        int newValue;

        // a switch statement would be five times as long, so let's go with ifs
        if (method == UpdateMethod.INCREMENT) newValue = this.value + value;
        else if (method == UpdateMethod.DECREMENT) newValue = this.value - value;
        else if (method == UpdateMethod.SET) newValue = value;

        // this really has no way of happening, but it tells Java that newValue must be initialized
        else throw new IllegalArgumentException("Error: unrecognized update method: " + method);

        // update if able, notify if unable
        if (newValue >= this.minValue && newValue <= this.maxValue) this.value = newValue;
        else throw new IllegalArgumentException("Error: value " + newValue + " is out of bounds for sensor");

    }

    public abstract boolean hasError(int refValue);

}

