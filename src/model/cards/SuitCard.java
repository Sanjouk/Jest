package model.cards;

import java.io.Serializable;

public class SuitCard extends Card implements Serializable {
    private Suit suit;
    private Face face;
    private static final long serialVersionUID = 1L;


    public SuitCard(boolean isTrophy, Suit suit, Face face) {
        super(isTrophy);
        this.suit = suit;
        this.face = face;
    }

    public Suit getSuit() {
        return suit;
    }

    public Face getFace() {
        return face;
    }

    public boolean isAce() {
        return face == Face.ACE;
    }
    public boolean isBlack() {
        return suit == Suit.CLUBS || suit == Suit.SPADES;
    }

    @Override
    public int getFaceValue() {
        return face.getFaceValue();
    }


    public int getSuitValue(){
        return suit.getStrength();
    }


    @Override
    public String toString() {
        return this.face + " " + this.suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuitCard suitCard = (SuitCard) o;

        return suit == suitCard.suit &&
                face == suitCard.face;
    }

}
