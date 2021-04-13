package com.example.sensors.api.provider;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YamlInitializerTest {

    private static YamlInitializer initializer;

    /*
        We can't really test the download, as that would require an always available, unchanging file to download
        from somewhere. Therefore, we'll simply create a configuration file ourselves, and then remove it once
        testing is done.
     */
    @BeforeAll
    public static void createFile() {

        initializer = new YamlInitializer();
        // TODO - create file
    }

    @Test
    void parseFileAndCreateSensorsTest() {
    }

    @Test
    void createEnginesFromSensorsTest() {
    }


    @AfterAll
    public static void removeFile() {
        // TODO - remove the previously created file
    }
}