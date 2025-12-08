package core;

import core.cards.Card;
import core.cards.Deck;
import core.cards.SuitCard;
import core.cards.Joker;
import core.players.HumanPlayer;
import core.players.Player;
import core.players.Offer;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        HumanPlayer player1 = new HumanPlayer("Alex", false);
        HumanPlayer player2 = new HumanPlayer("Jack", false);
        HumanPlayer player3 = new HumanPlayer("John", false);

        Deck deck = new Deck();
        System.out.println("Deck has " + deck.getRemainingCount() + " cards.");

        ArrayList<Card> trophies = deck.chooseTrophies(3);
        System.out.println("Chosen trophies:");
        trophies.forEach(System.out::println);
        System.out.println("\n");

        player1.addToHand(deck.dealCard());
        player1.addToHand(deck.dealCard());
        player2.addToHand(deck.dealCard());
        player2.addToHand(deck.dealCard());
        player3.addToHand(deck.dealCard());
        player3.addToHand(deck.dealCard());

        System.out.println("\n=== MAKE OFFERS ===");
        player1.makeOffer();
        player2.makeOffer();
        player3.makeOffer();

        ArrayList<Offer> offers = new ArrayList<>();
        offers.add(player1.getOffer());
        offers.add(player2.getOffer());
        offers.add(player3.getOffer());

        System.out.println("\n=== CHOOSE CARDS ===");
        player1.chooseCard(offers);
        player2.chooseCard(offers);
        player3.chooseCard(offers);

        System.out.println("\n=== FINAL JESTS ===");
        System.out.println(player1.getName() + "'s Jest: " + player1.getJest());
        System.out.println(player2.getName() + "'s Jest: " + player2.getJest());
        System.out.println(player3.getName() + "'s Jest: " + player3.getJest());



    }
}
