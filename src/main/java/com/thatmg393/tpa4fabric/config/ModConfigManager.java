package com.thatmg393.usefulhuds.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thatmg393.usefulhuds.UsefulHUDs;
import com.thatmg393.usefulhuds.config.data.ModConfigData;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class ModConfigManager {
    private static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    public static final File CONFIG_PATH = new File(Paths.get(
            FabricLoader.getInstance().getConfigDir().toString(),
            UsefulHUDs.MOD_ID + ".json"
        ).toString()
    );

    private static ModConfigData defaultConfig = new ModConfigData();
    private static ModConfigData loadedConfig;

    public static ModConfigData loadOrGetConfig() {
        if (loadedConfig != null) {
            return loadedConfig;
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(CONFIG_PATH));
            ModConfigData parsedConfig = GSON.fromJson(bufferedReader, ModConfigData.class);

            loadedConfig = parsedConfig;
        } catch (FileNotFoundException e) {
            UsefulHUDs.LOGGER.error("An exception occurred! " + e.toString());

            UsefulHUDs.LOGGER.info("Using default config instead...");
            loadedConfig = new ModConfigData();
            saveConfig();
        }

        return loadedConfig;
    }

    public static ModConfigData getDefaultConfig() {
        return defaultConfig;
    }

    public static void saveConfig() {
        try (FileWriter fileWriter = new FileWriter(CONFIG_PATH)) {
            String serializedJson = GSON.toJson(loadedConfig);
            fileWriter.write(serializedJson);
        } catch (IOException e) {
            UsefulHUDs.LOGGER.error(e.toString());
        }
    }
}