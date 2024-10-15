package com.thatmg393.tpa4fabric.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thatmg393.tpa4fabric.TPA4Fabric;
import com.thatmg393.tpa4fabric.config.data.ModConfigData;

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
            TPA4Fabric.MOD_ID + ".json"
        ).toString()
    );

    private static ModConfigData defaultConfig = new ModConfigData();
    private static ModConfigData loadedConfig;

    public static ModConfigData loadOrGetConfig() {
        if (loadedConfig != null)
            return loadedConfig;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(CONFIG_PATH));
            ModConfigData parsedConfig = GSON.fromJson(bufferedReader, ModConfigData.class);

            if (parsedConfig.configVersion != defaultConfig.configVersion)
                TPA4Fabric.LOGGER.warn("Config versions DO NOT MATCH!"); // TODO? : do smth with old conf ver
            
            loadedConfig = parsedConfig;
        } catch (FileNotFoundException e) {
            TPA4Fabric.LOGGER.error("An exception occurred! " + e.toString());

            TPA4Fabric.LOGGER.info("Using default config instead...");
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
            TPA4Fabric.LOGGER.error(e.toString());
        }
    }
}