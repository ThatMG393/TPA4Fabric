package com.thatmg393.tpa4fabric.utils;

import org.slf4j.helpers.MessageFormatter;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class MCTextUtils {
    public static MutableText textOf(String s) {
        return Text.literal(s);
    }

    public static MutableText textOf(String s, Object... formats) {
        return Text.literal(MessageFormatter.basicArrayFormat(s, formats));
    }

    public static MutableText fromLang(String key) {
        return textOf(fromLangAsStr(key));
    }

    public static String fromLangAsStr(String key) {
        return Text.translatable(key).getString();
    }

    public static MutableText fromLang(String key, Object... formats) {
        return textOf(fromLangAsStr(key), formats);
    }
}
