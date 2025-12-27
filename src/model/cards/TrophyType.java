package model.cards;

import java.io.Serializable;

public enum TrophyType implements Serializable {
    NONE("None"),
    HIGHEST_FACE("Highest Face"),
    LOWEST_FACE("Lowest Face"),
    MAJORITY_FACE_VALUE("Majority Face Value"),
    JOKER("Joker"),
    BEST_JEST("Best Jest"),
    BEST_JEST_NO_JOKER("Best Jest without Joker");

    private static final long serialVersionUID = 1L;

    private String name;

    TrophyType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;

    }

}
