package core;

import consoleUI.RoundView;
import core.game.*;
import core.cards.Card;
import core.cards.Deck;
import core.players.HumanPlayer;
import core.players.Player;

import java.util.ArrayList;


public class TestRound {

    public static void main(String[] args) {
        System.out.println("### 🎯 Тестирование класса Round ###");
        System.out.println("------------------------------------");

        Deck deck = new Deck();

        ArrayList<Player> players = new ArrayList<>();
        HumanPlayer player1 = new HumanPlayer("Alice", false);
        HumanPlayer player2 = new HumanPlayer("Bob", false);
        HumanPlayer player3 = new HumanPlayer("Charlie", false);

        players.add(player1);
        players.add(player2);
        players.add(player3);

        Round round = new Round(players, deck, new RoundView());

        System.out.println("✅ Round создан с " + players.size() + " игроками и Deck.");
        System.out.println("------------------------------------");


        System.out.println("### 🃏 Тестирование dealCards() ###");
        round.dealCards();

        System.out.println("### 🃏 Тестирование makeOffers() ###");
        round.makeOffers();

        System.out.println("\n--- Проверка первого игрока который начинает ---");
        Player firstPlayer = round.determineStartingPlayer();
        System.out.println(firstPlayer.getName());

        // 6.
        System.out.println("### 🃏 Тестирование playChoosingPhase() ###");
        round.playChoosingPhase(firstPlayer);


        // 7.
        System.out.println("### 🃏 Тестирование returnRemainingCardToDeck() ###");
        round.returnRemainingCardsToDeck();


    }
}