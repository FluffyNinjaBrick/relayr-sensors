package com.example.sensors.api.update;

import lombok.Data;

@Data
public class UpdateHelper {

    private UpdateMethod operation;
    private int value;

}
