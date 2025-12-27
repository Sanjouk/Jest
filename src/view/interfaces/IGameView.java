package view.interfaces;

import model.players.Player;
import model.players.strategies.StrategyType;

import java.util.List;

public interface IGameView {
    int askNumberOfPlayers();
    String askPlayerName(int playerNumber);
    StrategyType askStrategy(String name);
    boolean isHumanPlayer(String name);
    void showPlayers(List<Player> players);
    void showRound(int roundNumber);
    void showTrophies(String trophiesInfo);
    void showScore(Player player);
    void showWinner(Player winner);
    void showWinners(List<Player> winners);
    void showEndRoundMessage();
}

