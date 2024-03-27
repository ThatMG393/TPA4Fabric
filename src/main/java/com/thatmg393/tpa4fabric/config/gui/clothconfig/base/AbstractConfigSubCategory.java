package com.thatmg393.usefulhuds.config.gui.clothconfig.base;

import java.util.ArrayList;

import com.thatmg393.usefulhuds.config.data.ModConfigData;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.SubCategoryListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.text.Text;

public abstract class AbstractConfigSubCategory {
    public final Text title;
    public final ConfigEntryBuilder entryBuilder;

    public final ModConfigData config;
    public final ModConfigData defaultConfig;

    public AbstractConfigSubCategory(Text title, AbstractConfigCategory cc) {
        this.title = title;
        this.entryBuilder = cc.entryBuilder;
        this.config = cc.config;
        this.defaultConfig = cc.defaultConfig;
    }

    public AbstractConfigSubCategory(Text title, AbstractConfigSubCategory csc) {
        this.title = title;
        this.entryBuilder = csc.entryBuilder;
        this.config = csc.config;
        this.defaultConfig = csc.defaultConfig;
    }

    @SuppressWarnings("rawtypes")
    public abstract ArrayList<AbstractConfigListEntry> getEntries();

    public SubCategoryListEntry build() {
        SubCategoryBuilder scle = entryBuilder.startSubCategory(title);
        scle.addAll(getEntries());
        return scle.build();
    }
}
