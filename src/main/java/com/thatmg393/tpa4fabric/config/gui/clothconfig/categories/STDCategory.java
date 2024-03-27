package com.thatmg393.usefulhuds.config.gui.clothconfig.categories;

import java.util.ArrayList;

import com.thatmg393.usefulhuds.config.gui.clothconfig.base.AbstractConfigCategory;
import com.thatmg393.usefulhuds.utils.DisplayUtils;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.text.Text;

@SuppressWarnings("rawtypes")
public class STDCategory extends AbstractConfigCategory {
    public STDCategory() {
        super(Text.translatable("usefulhuds.config.std.title"));
    }

    @Override
    public ArrayList<AbstractConfigListEntry> getEntries() {
        ArrayList<AbstractConfigListEntry> entries = new ArrayList<>();

        entries.add(
            entryBuilder.startBooleanToggle(
                Text.translatable("usefulhuds.config.hudvisible"),
                config.STD.visible
            ).setDefaultValue(defaultConfig.STD.visible)
            .setSaveConsumer(v -> config.STD.visible = v)
            .build()
        );

        entries.add(
            entryBuilder.startColorField(
                Text.translatable("usefulhuds.config.hudtextcolor"),
                config.STD.textColor
            ).setDefaultValue(config.STD.textColor)
            .setSaveConsumer(v -> config.STD.textColor = v)
            .build()
        );

        entries.add(
            entryBuilder.startIntSlider(
                Text.translatable("usefulhuds.config.hudoffsetx"),
                config.STD.offsetX,
                0, DisplayUtils.getScreenScaledWH()[0]
            ).setDefaultValue(defaultConfig.STD.offsetX)
            .setSaveConsumer(v -> config.STD.offsetX = v)
            .build()
        );

        entries.add(
            entryBuilder.startIntSlider(
                Text.translatable("usefulhuds.config.hudoffsety"),
                config.STD.offsetY,
                0, DisplayUtils.getScreenScaledWH()[1]
            ).setDefaultValue(defaultConfig.STD.offsetX)
            .setSaveConsumer(v -> config.STD.offsetY = v)
            .build()
        );

        entries.add(
            entryBuilder.startFloatField(
                Text.translatable("usefulhuds.config.hudscale"),
                config.STD.scale
            ).setDefaultValue(defaultConfig.STD.scale)
            .setSaveConsumer(v -> config.STD.scale = v)
            .build()
        );

        return entries;
    }
}
