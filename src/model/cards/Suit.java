package model.cards;

import java.io.Serializable;

public enum Suit implements Serializable {
    DIAMONDS(1),
    HEARTS(2),
    CLUBS(3),
    SPADES(4);

    private static final long serialVersionUID = 1L;

    private final int strength;
    Suit(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }
}
