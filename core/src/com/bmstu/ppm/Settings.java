package com.bmstu.ppm;

import com.badlogic.gdx.utils.Json;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Settings {
    public int volume;
    public int graphics;
    public int FOV;

    public void save() throws IOException {
        Json json = new Json();
        FileWriter writer = new FileWriter("settings.json", false);
        writer.write(json.toJson(this));
        writer.close();
    }

    public void load() throws IOException {
        Json json = new Json();
        File file = new File("settings.json");
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String str = new String(data, StandardCharsets.UTF_8);
        Settings s = json.fromJson(Settings.class, str);
        this.volume = s.volume;
        this.FOV = s.FOV;
        this.graphics = s.graphics;
    }
}
