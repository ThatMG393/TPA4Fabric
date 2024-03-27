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
        return textOf(fromLangAsStr(key));
    }

    public static String fromLangAsStr(String key) {
        return Text.translatable(key).getString();
    }

    public static Text fromLang(String key, Object... formats) {
        return textOf(fromLangAsStr(key), formats);
    }
}
