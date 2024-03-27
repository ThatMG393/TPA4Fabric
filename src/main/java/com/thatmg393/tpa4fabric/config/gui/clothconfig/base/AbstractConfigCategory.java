package com.thatmg393.usefulhuds.config.gui.clothconfig.base;

import java.util.ArrayList;

import com.thatmg393.usefulhuds.config.ModConfigManager;
import com.thatmg393.usefulhuds.config.data.ModConfigData;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public abstract class AbstractConfigCategory {
    public final Text title;

    public final ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

    public final ModConfigData config;
    public final ModConfigData defaultConfig;

    public AbstractConfigCategory(Text title) {
        this.title = title;
        this.config = ModConfigManager.loadOrGetConfig();
        this.defaultConfig = ModConfigManager.getDefaultConfig();
    }

    @SuppressWarnings("rawtypes")
    public abstract ArrayList<AbstractConfigListEntry> getEntries();

    public void build(ConfigBuilder configBuilder) {
        ConfigCategory cc = configBuilder.getOrCreateCategory(title);
        getEntries().forEach(e -> cc.addEntry(e));
    }
}
