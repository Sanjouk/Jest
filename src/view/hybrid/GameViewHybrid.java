package view.hybrid;

import model.cards.ExtensionCard;
import model.players.Player;
import model.players.strategies.StrategyType;
import view.console.GameView;
import view.gui.GameViewGUI;
import view.interfaces.IGameView;

import java.util.ArrayList;
import java.util.List;

public class GameViewHybrid implements IGameView {
    private final GameView consoleView;
    private final GameViewGUI guiView;

    public GameViewHybrid(GameView consoleView, GameViewGUI guiView) {
        this.consoleView = consoleView;
        this.guiView = guiView;
    }

    @Override
    public int askNumberOfPlayers() {
        // Use console for input in hybrid mode (setup questions)
        int result = consoleView.askNumberOfPlayers();
        return result;
    }

    @Override
    public String askPlayerName(int playerNumber) {
        // Use console for input in hybrid mode
        return consoleView.askPlayerName(playerNumber);
    }

    @Override
    public StrategyType askStrategy(String name) {
        // Use console for input in hybrid mode
        return consoleView.askStrategy(name);
    }

    @Override
    public boolean isHumanPlayer(String name) {
        // Use console for input in hybrid mode
        return consoleView.isHumanPlayer(name);
    }

    @Override
    public ArrayList<Integer> askForExtensions(ArrayList<ExtensionCard> availableExtensions) {
        // In hybrid mode, use GUI for extension selection (more visual and user-friendly)
        return guiView.askForExtensions(availableExtensions);
    }

    @Override
    public void showInvalidExtensionMessage(String message) {
        // Show error in both console and GUI
        consoleView.showInvalidExtensionMessage(message);
        guiView.showInvalidExtensionMessage(message);
    }

    @Override
    public void showPlayers(List<Player> players) {
        consoleView.showPlayers(players);
        guiView.showPlayers(players);
    }

    @Override
    public void showRound(int roundNumber) {
        consoleView.showRound(roundNumber);
        guiView.showRound(roundNumber);
    }

    @Override
    public void showTrophies(String trophiesInfo) {
        consoleView.showTrophies(trophiesInfo);
        guiView.showTrophies(trophiesInfo);
    }

    @Override
    public void showScore(Player player) {
        consoleView.showScore(player);
        guiView.showScore(player);
    }

    @Override
    public void showWinner(Player winner) {
        consoleView.showWinner(winner);
        guiView.showWinner(winner);
    }

    @Override
    public void showWinners(List<Player> winners) {
        consoleView.showWinners(winners);
        guiView.showWinners(winners);
    }

    @Override
    public void showEndRoundMessage() {
        consoleView.showEndRoundMessage();
        guiView.showEndRoundMessage();
    }
}