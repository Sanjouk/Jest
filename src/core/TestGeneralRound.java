package core;

import consoleUI.RoundView;
import core.cards.Deck;
import core.game.Round;
import core.players.HumanPlayer;
import core.players.Player;
import core.players.ScoreVisitorImpl;

import java.util.ArrayList;

public class TestGeneralRound {
    public static void main(String[] args) {
        Deck deck = new Deck();

        ArrayList<Player> players = new ArrayList<>();
        HumanPlayer player1 = new HumanPlayer("Alice", false);
        HumanPlayer player2 = new HumanPlayer("Bob", false);
        HumanPlayer player3 = new HumanPlayer("Charlie", false);

        players.add(player1);
        players.add(player2);
        players.add(player3);

        // 2. Создание экземпляра Round
        Round round = new Round(players, deck, new RoundView());

        round.playRound();
        ScoreVisitorImpl scoreVisitor = new ScoreVisitorImpl();
        player1.calculateScore(scoreVisitor);
        System.out.println(player1.getName() + " has score: " + player1.getScore());
        player2.calculateScore(scoreVisitor);
        System.out.println(player2.getName() + " has score: " + player2.getScore());
        player3.calculateScore(scoreVisitor);
        System.out.println(player3.getName() + " has score: " + player3.getScore());
    }
}
