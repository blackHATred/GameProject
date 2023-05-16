package com.bmstu.ppm;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Null;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Settings {
    public int volume;
    public int graphics;
    public int FOV;
    public boolean fishEye;

    public static class Save {
        public STAGE level;
    }

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
        this.fishEye = s.fishEye;
    }

    public static void save_save(Save save, @Null String fileName) throws IOException {
        Json json = new Json();
        FileWriter writer = new FileWriter(fileName == null ? "PPMSave.json" : fileName, false);
        writer.write(json.toJson(save));
        writer.close();
    }

    public static STAGE load_save(String path) throws IOException {
        Json json = new Json();
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String str = new String(data, StandardCharsets.UTF_8);
        Save s = json.fromJson(Save.class, str);
        return s.level;
    }
}
