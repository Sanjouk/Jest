package view.gui;

import model.cards.Card;
import model.players.Offer;
import model.players.Player;
import view.interfaces.IRoundView;

import javax.swing.*;

public class RoundViewGUI implements IRoundView {
    private final JTextArea outputArea;

    public RoundViewGUI(JTextArea outputArea) {
        this.outputArea = outputArea;
    }

    private void appendOutput(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    @Override
    public void showRoundStart() {
        appendOutput("Round Started");
    }

    @Override
    public void showDealCards() {
        appendOutput("Deal cards to players");
    }

    @Override
    public void showMakeOffers() {
        appendOutput("Players make offers");
    }

    @Override
    public void showDetermineStartingPlayer() {
        appendOutput("Determine starting player");
    }

    @Override
    public void showStartingPlayer(Player p, Card faceUpCard) {
        appendOutput("First to play: " + p.getName() + " (face-up card: " + faceUpCard + ")");
    }

    @Override
    public void showChoosingPhaseStart() {
        appendOutput("\n--- CHOOSING CARDS PHASE ---");
    }

    @Override
    public void showTurn(Player p) {
        appendOutput("\nIt's " + p.getName() + "'s turn to choose a card.");
    }

    @Override
    public void showCardTaken(Player player, Offer takenOffer, Player next) {
        appendOutput(player.getName() + " took " + player.getLastCard() +
                " from " + takenOffer.getOwner().getName() +
                " → next player: " + next.getName());
    }

    @Override
    public void showLastCardTaken(Player player, Offer takenOffer) {
        appendOutput(player.getName() + " took " + player.getLastCard() +
                " → from: " + takenOffer.getOwner().getName());
    }

    @Override
    public void showDeckEmpty() {
        appendOutput("Deck is empty, finalizing round...");
    }

    @Override
    public void showRoundEnd() {
        appendOutput("Round has ended");
    }

    @Override
    public void showNoOffers() {
        appendOutput("No valid offers found. Defaulting to first player.");
    }
}

