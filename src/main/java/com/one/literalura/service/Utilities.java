package com.one.literalura.service;

public class Utilities {
    public static String reversedName(String name) {
        if (!name.contains(",")) {
            return name.trim();
        } else {
            String[] parts = name.split(", ");
            return parts[1].trim() + " " + parts[0].trim();
        }
    }
}
