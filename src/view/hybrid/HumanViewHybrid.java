package view.hybrid;

import model.cards.Card;
import model.players.Offer;
import view.console.HumanView;
import view.gui.HumanViewGUI;
import view.interfaces.IHumanView;

import java.util.ArrayList;

public class HumanViewHybrid implements IHumanView {
    private final HumanView consoleView;
    private final HumanViewGUI guiView;

    public HumanViewHybrid(HumanView consoleView, HumanViewGUI guiView) {
        this.consoleView = consoleView;
        this.guiView = guiView;
    }

    @Override
    public int chooseFaceUpCard(String playerName, ArrayList<Card> hand) {
        // Use GUI for input, but also show in console
        consoleView.showMessage(playerName + " has " + hand.size() + " cards to make an offer");
        consoleView.showMessage("These are your cards:");
        for (int i = 0; i < hand.size(); i++) {
            consoleView.showMessage((i + 1) + ": " + hand.get(i));
        }
        // Use GUI for actual selection
        return guiView.chooseFaceUpCard(playerName, hand);
    }

    @Override
    public Offer chooseOffer(String playerName, ArrayList<Offer> selectableOffers) {
        // Show in console
        consoleView.chooseOffer(playerName, selectableOffers);
        // Use GUI for actual selection
        return guiView.chooseOffer(playerName, selectableOffers);
    }

    @Override
    public boolean chooseFaceUpOrDown() {
        // Show in console
        consoleView.chooseFaceUpOrDown();
        // Use GUI for actual selection
        return guiView.chooseFaceUpOrDown();
    }

    @Override
    public void showMessage(String message) {
        consoleView.showMessage(message);
        guiView.showMessage(message);
    }

    @Override
    public void hasNoEnoughCards(String name) {
        consoleView.hasNoEnoughCards(name);
        guiView.hasNoEnoughCards(name);
    }

    @Override
    public void thankForChoosing(Card faceUpCard, Card faceDownCard) {
        consoleView.thankForChoosing(faceUpCard, faceDownCard);
        guiView.thankForChoosing(faceUpCard, faceDownCard);
    }
}

