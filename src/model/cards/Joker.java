package model.cards;

import java.io.Serializable;

public class Joker extends Card implements Serializable {
    private static final long serialVersionUID = 1L;

    public Joker(boolean isTrophy) {
        super(isTrophy);
    }

    @Override
    public int getFaceValue() {
        return 0;
    }

    @Override
    public int getSuitValue() {
        return 0;
    }

    @Override
    public String toString() {
        return "Joker";
    }
}
