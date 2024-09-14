package net.graysenko.tNTBooster.Util;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {
    // Метод для обработки HEX-кодов (градиентов)
    public static String parseGradient(String text) {
        if (text == null) return null;

        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group(1);
            String color = ChatColor.of("#" + hexCode).toString();
            matcher.appendReplacement(buffer, color);
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    public static String parseAllColors(String text) {
        return ChatColor.translateAlternateColorCodes('&', parseGradient(text));
    }


    public static List<String> parseLoreColors(List<String> lore) {
        if (lore == null) return null;
        List<String> coloredLore = new ArrayList<>();
        for (String line : lore) {
            coloredLore.add(parseAllColors(line)); // Обрабатываем каждый элемент списка
        }
        return coloredLore;
    }
}

