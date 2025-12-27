package model.cards;

import java.io.Serializable;

public enum Face implements Serializable {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private static final long serialVersionUID = 1L;

    private final int faceValue;

    Face(int faceValue) {
        this.faceValue = faceValue;
    }

    public int getFaceValue() {
        return faceValue;
    }
}
