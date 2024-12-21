package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(
        Long id,
        String title,
        Set<String> subjects,
        Set<PersonData> authors,
        Set<PersonData> translators,
        Set<String> bookshelves,
        Set<String> languages,
        Boolean copyright,
        @JsonAlias("media_type") String mediaType,
        Map<String, String> formats,
        @JsonAlias("download_count") Integer downloadCount
) {
    public Language getPrimaryLanguage() {
        if (languages != null && !languages.isEmpty()) {
            String primaryLanguageCode = languages.iterator().next();
            return Language.fromString(primaryLanguageCode);
        }
        throw new IllegalStateException("No language available for the book");
    }
}
