package com.hotbarignore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hotbarignore.HotbarIgnore;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class HotbarIgnoreConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("hotbarignore.json");
    private static HotbarIgnoreConfig INSTANCE;

    public boolean enabled = true;
    public boolean allowKeypad = true;

    public static HotbarIgnoreConfig getInstance() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                INSTANCE = GSON.fromJson(reader, HotbarIgnoreConfig.class);
            } catch (Exception e) {
                HotbarIgnore.LOGGER.error("Failed to load HotbarIgnore configuration, resetting to defaults", e);
            }
        }

        if (INSTANCE == null) {
            INSTANCE = new HotbarIgnoreConfig();
            save();
        }
    }

    public static void save() {
        try {
            if (INSTANCE == null) {
                INSTANCE = new HotbarIgnoreConfig();
            }
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (IOException e) {
            HotbarIgnore.LOGGER.error("Failed to save HotbarIgnore configuration", e);
        }
    }
}
