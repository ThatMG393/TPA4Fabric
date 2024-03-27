package com.thatmg393.usefulhuds.config.gui.clothconfig;

import com.thatmg393.usefulhuds.UsefulHUDs;
import com.thatmg393.usefulhuds.config.ModConfigManager;
import com.thatmg393.usefulhuds.config.gui.clothconfig.categories.FPSCategory;
import com.thatmg393.usefulhuds.config.gui.clothconfig.categories.STDCategory;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModClothConfigScreen {
    public static Screen getConfigGui(Screen parent) {
        ConfigBuilder configBuilder = ConfigBuilder.create();
        configBuilder.setParentScreen(parent);
        configBuilder.setTitle(Text.translatable("usefulhuds.config.title"));
        configBuilder.setSavingRunnable(() -> {
            UsefulHUDs.LOGGER.info("Saving configs...");
            ModConfigManager.saveConfig();
        });

        new FPSCategory().build(configBuilder);
        new STDCategory().build(configBuilder);

        return configBuilder.build();
    }
}
