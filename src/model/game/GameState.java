package model.game;

import model.cards.Card;
import model.players.Player;
import model.players.HumanPlayer;
import model.players.VirtualPlayer;
import model.players.strategies.StrategyType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the complete state of the game that can be saved and loaded.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    // Player information
    private ArrayList<PlayerData> playersData;

    // Deck state
    private ArrayList<Card> remainingCards;

    // Trophy information
    private ArrayList<Card> trophies;

    // Round counter
    private int roundCounter;

    public GameState() {
        this.playersData = new ArrayList<>();
        this.remainingCards = new ArrayList<>();
        this.trophies = new ArrayList<>();
        this.roundCounter = 0;
    }

    // Getters and setters
    public ArrayList<PlayerData> getPlayersData() {
        return playersData;
    }

    public void setPlayersData(ArrayList<PlayerData> playersData) {
        this.playersData = playersData;
    }

    public ArrayList<Card> getRemainingCards() {
        return remainingCards;
    }

    public void setRemainingCards(ArrayList<Card> remainingCards) {
        this.remainingCards = remainingCards;
    }

    public ArrayList<Card> getTrophies() {
        return trophies;
    }

    public void setTrophies(ArrayList<Card> trophies) {
        this.trophies = trophies;
    }

    public int getRoundCounter() {
        return roundCounter;
    }

    public void setRoundCounter(int roundCounter) {
        this.roundCounter = roundCounter;
    }

    /**
     * Inner class to hold player data
     */
    public static class PlayerData implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private boolean isHuman;
        private StrategyType strategy; // null for human players
        private ArrayList<Card> jestCards;
        private int score;

        public PlayerData(String name, boolean isHuman, StrategyType strategy,
                          ArrayList<Card> jestCards, int score) {
            this.name = name;
            this.isHuman = isHuman;
            this.strategy = strategy;
            this.jestCards = new ArrayList<>(jestCards);
            this.score = score;
        }

        // Getters
        public String getName() { return name; }
        public boolean isHuman() { return isHuman; }
        public StrategyType getStrategy() { return strategy; }
        public ArrayList<Card> getJestCards() { return jestCards; }
        public int getScore() { return score; }
    }

    /**
     * Creates a GameState from the current game
     */
    public static GameState fromGame(Game game, int currentRound) {
        GameState state = new GameState();

        // Save player data
        ArrayList<PlayerData> playersData = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            StrategyType strategy = null;
            if (player instanceof VirtualPlayer) {
                // You'll need to add a getStrategy() method to VirtualPlayer
                strategy = ((VirtualPlayer) player).getStrategyType();
            }

            PlayerData pd = new PlayerData(
                    player.getName(),
                    player instanceof HumanPlayer,
                    strategy,
                    player.getJest().getCards(),
                    player.getScore()
            );
            playersData.add(pd);
        }
        state.setPlayersData(playersData);

        // Save deck state
        state.setRemainingCards(new ArrayList<>(game.getDeck().getCards()));

        // Save trophies
        state.setTrophies(new ArrayList<>(game.getTrophies()));

        // Save round counter
        state.setRoundCounter(currentRound);

        return state;
    }

    /**
     * Restores a Game from this GameState
     */
    public Game toGame() {
        Game game = new Game();

        // Restore players
        for (PlayerData pd : playersData) {
            if (pd.isHuman()) {
                game.addHumanPlayer(pd.getName());
            } else {
                game.addVirtualPlayer(pd.getName(), pd.getStrategy());
            }

            // Restore jest cards and score
            Player player = game.getPlayers().get(game.getPlayers().size() - 1);
            for (Card card : pd.getJestCards()) {
                player.getJest().addCard(card);
            }
            player.setScore(pd.getScore());
        }

        // Restore deck
        game.getDeck().getCards().clear();
        for (Card card : remainingCards) {
            game.getDeck().addCard(card);
        }

        // Restore trophies
        game.setTrophies(new ArrayList<>(trophies));

        // Restore round counter
        Round.setRoundCounter(roundCounter);

        return game;
    }
}