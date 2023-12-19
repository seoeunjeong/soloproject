package soloproject.seomoim.moim.entitiy;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum MoimCategory {
    STUDY, EXERCISE, TRAVEL, EAT,
    DRINK, COFFEE, ONLINE;

    @JsonCreator
    public static MoimCategory fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return MoimCategory.valueOf(value);
    }
}
