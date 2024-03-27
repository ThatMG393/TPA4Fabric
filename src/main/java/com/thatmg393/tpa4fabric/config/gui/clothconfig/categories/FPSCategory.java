package com.thatmg393.usefulhuds.config.gui.clothconfig.categories;

import java.util.ArrayList;

import com.thatmg393.usefulhuds.config.gui.clothconfig.base.AbstractConfigCategory;
import com.thatmg393.usefulhuds.config.gui.clothconfig.base.AbstractConfigSubCategory;
import com.thatmg393.usefulhuds.utils.DisplayUtils;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.text.Text;

@SuppressWarnings("rawtypes")
public class FPSCategory extends AbstractConfigCategory {
    public class SubCategory extends AbstractConfigSubCategory {
        public SubCategory() {
            super(Text.translatable("usefulhuds.config.fps.advanced.name"), FPSCategory.this);
        }

        @Override
        public ArrayList<AbstractConfigListEntry> getEntries() {
            ArrayList<AbstractConfigListEntry> entries = new ArrayList<>();

            entries.add(
                entryBuilder.startBooleanToggle(
                    Text.translatable("usefulhuds.config.fps.advanced.showminavgmax"),
                    config.FPS.ADVANCED.showAdvanceInfo
                ).setDefaultValue(defaultConfig.FPS.ADVANCED.showAdvanceInfo)
                .setSaveConsumer(v -> config.FPS.ADVANCED.showAdvanceInfo = v)
                .build()
            );

            return entries;
        }
    }

    private SubCategory ADVANCED = new SubCategory();

    public FPSCategory() {
        super(Text.translatable("usefulhuds.config.fps.title"));
    }

    @Override
    public ArrayList<AbstractConfigListEntry> getEntries() {
        ArrayList<AbstractConfigListEntry> entries = new ArrayList<>();

        entries.add(
            entryBuilder.startBooleanToggle(
                Text.translatable("usefulhuds.config.hudvisible"),
                config.FPS.visible
            ).setDefaultValue(defaultConfig.FPS.visible)
            .setSaveConsumer(nv -> config.FPS.visible = nv)
            .build()
        );

        entries.add(
            entryBuilder.startColorField(
                Text.translatable("usefulhuds.config.hudtextcolor"),
                config.FPS.textColor
            ).setDefaultValue(defaultConfig.FPS.textColor)
            .setSaveConsumer(nv -> config.FPS.textColor = nv)
            .build()
        );

        entries.add(
            entryBuilder.startIntSlider(
                Text.translatable("usefulhuds.config.hudoffsetx"),
                config.FPS.offsetX,
                0, DisplayUtils.getScreenScaledWH()[0]
            ).setDefaultValue(defaultConfig.FPS.offsetX)
            .setSaveConsumer(v -> config.FPS.offsetX = v)
            .build()
        );

        entries.add(
            entryBuilder.startIntSlider(
                Text.translatable("usefulhuds.config.hudoffsety"),
                config.FPS.offsetY,
                0, DisplayUtils.getScreenScaledWH()[1]
            ).setDefaultValue(defaultConfig.FPS.offsetX)
            .setSaveConsumer(v -> config.FPS.offsetY = v)
            .build()
        );

        entries.add(
            entryBuilder.startFloatField(
                Text.translatable("usefulhuds.config.hudscale"),
                config.FPS.scale
            ).setDefaultValue(defaultConfig.FPS.scale)
            .setSaveConsumer(v -> config.FPS.scale = v)
            .build()
        );

        entries.add(ADVANCED.build());
        return entries;
    }
}
