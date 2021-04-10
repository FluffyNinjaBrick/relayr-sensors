package com.example.sensors.api.rest;

import com.example.sensors.api.provider.DataProvider;
import com.example.sensors.api.update.UpdateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final DataProvider provider;

    @Autowired
    public RestController(DataProvider provider) {
        this.provider = provider;
    }

    @GetMapping("/engines")
    public List<String> getMalfunctioningEngines(@RequestParam int minPressure, @RequestParam int maxTemperature) {
        return this.provider.getMalfunctioningEngines(minPressure, maxTemperature);
    }

    @PostMapping("/sensors/{id}")
    public void updateSensorValue(@PathVariable String id, @RequestBody UpdateHelper helper) {
        this.provider.updateSensor(id, helper.getValue(), helper.getOperation());
    }
}
