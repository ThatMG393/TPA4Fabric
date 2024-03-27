package com.thatmg393.tpa4fabric.utils;

import org.slf4j.helpers.MessageFormatter;

import net.minecraft.text.Text;

public class MCTextUtils {
    public static Text textOf(String s) {
        return Text.of(s);
    }

    public static Text textOf(String s, Object... formats) {
        return Text.of(MessageFormatter.format(s, formats).getMessage());
    }

    public static Text fromLang(String key) {
        return Text.translatable(key);
    }

    public static Text fromLang(String key, Object... formats) {
        String s = Text.translatable(key).getString();
        return textOf(s, formats);
    }
}
