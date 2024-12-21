package com.alura.literalura.model;

public enum Language {
    ZH("chino"),
    EN("inglés"),
    ES("español"),
    AR("árabe"),
    FR("francés"),
    RU("ruso"),
    PT("portugués"),
    DE("alemán"),
    JA("japonés"),
    IT("italiano"),
    PL("polaco"),
    FA("persa"),
    HE("hebreo"),
    EL("griego");

    private final String fullName;

    Language(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public static Language fromString(String text) {
        for (Language language : Language.values()) {
            if (language.fullName.equalsIgnoreCase(text) || language.name().equalsIgnoreCase(text)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Idioma no encontrado: " + text);
    }
}
