package com.one.literalura.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Language {
    ENGLISH("en","Ingles"),
    SPANISH("es","Español"),
    DEUTSCH("de", "Aleman"),
    FRENCH("fr", "Frances"),
    ITALIAN("it", "Italiano"),
    PORTUGUESE("pt", "Portugues"),
    RUSSIAN ("ru","Ruso"),
    FILIPINO("tl", "Filipino"),
    HUNGARIAN("hu", "Hungaro"),

    POLISH("pl", "Polaco");
    private String languageAbbreviation;
    private String languageSpanish;

    Language(String languageAbbreviation, String languageSpanish) {
        this.languageAbbreviation = languageAbbreviation;
        this.languageSpanish = languageSpanish;
    }
    public static Language fromAbbreviation (String text) {
        for (Language lan : Language.values()){
            if(lan.languageAbbreviation.equalsIgnoreCase(text)){
                return lan;
            }
        }
        throw new IllegalArgumentException("Ninguna idioma encontrada: " + text);
    }
    public static Language fromSpanish(String text) {
        for (Language lan : Language.values()) {
            if (lan.languageSpanish.equalsIgnoreCase(text)) {
                return lan;
            }
        }
        throw new IllegalArgumentException("Ningún idioma encontrado: " + text);
    }
    public static Map<String, String> getLanguageMap() {
        Map<String, String> map = new HashMap<>();
        for (Language language : Language.values()) {
            map.put(language.languageAbbreviation, language.languageAbbreviation + " - " + language.languageSpanish);
        }
        return map;
    }
}