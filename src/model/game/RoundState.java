package model.game;

import model.cards.Card;
import model.players.Offer;
import model.players.Player;
import model.players.VirtualPlayer;
import model.players.strategies.StrategyType;

import java.io.Serializable;
import java.util.ArrayList;

public class RoundState implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<GameState.PlayerData> playersData;

    private ArrayList<Card> remainingCards;
    private ArrayList<Card> trophies;
    private int roundCounter;

    private ArrayList<Card> playerHandCards;

    private ArrayList<OfferData> offers;
    private ArrayList<String> alreadyPlayedPlayerNames;
    private String currentPlayerName;

    public RoundState() {
        this.playersData = new ArrayList<>();
        this.remainingCards = new ArrayList<>();
        this.trophies = new ArrayList<>();
        this.roundCounter = 0;
        this.playerHandCards = new ArrayList<>();
        this.offers = new ArrayList<>();
        this.alreadyPlayedPlayerNames = new ArrayList<>();
        this.currentPlayerName = null;
    }

    public ArrayList<GameState.PlayerData> getPlayersData() {
        return playersData;
    }

    public ArrayList<Card> getRemainingCards() {
        return remainingCards;
    }

    public ArrayList<Card> getTrophies() {
        return trophies;
    }

    public int getRoundCounter() {
        return roundCounter;
    }

    public ArrayList<Card> getPlayerHandCards() {
        return playerHandCards;
    }

    public ArrayList<OfferData> getOffers() {
        return offers;
    }

    public ArrayList<String> getAlreadyPlayedPlayerNames() {
        return alreadyPlayedPlayerNames;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public static class OfferData implements Serializable {
        private static final long serialVersionUID = 1L;

        private String ownerName;
        private Card faceUpCard;
        private Card faceDownCard;

        public OfferData(String ownerName, Card faceUpCard, Card faceDownCard) {
            this.ownerName = ownerName;
            this.faceUpCard = faceUpCard;
            this.faceDownCard = faceDownCard;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public Card getFaceUpCard() {
            return faceUpCard;
        }

        public Card getFaceDownCard() {
            return faceDownCard;
        }
    }

    public static RoundState from(Game game, Round round, Player currentPlayer) {
        RoundState state = new RoundState();

        state.playersData = GameState.fromGame(game, Round.getRoundCounter()).getPlayersData();
        state.remainingCards = new ArrayList<>(game.getDeck().getCards());
        state.trophies = new ArrayList<>(game.getTrophies());
        state.roundCounter = Round.getRoundCounter();

        state.playerHandCards = new ArrayList<>();
        for (Player p : game.getPlayers()) {
            state.playerHandCards.addAll(p.getHand());
        }

        state.offers = new ArrayList<>();
        for (Offer offer : round.getOffers()) {
            if (offer == null || offer.getOwner() == null) continue;
            state.offers.add(new OfferData(
                    offer.getOwner().getName(),
                    offer.getFaceUpCard(),
                    offer.getFaceDownCard()
            ));
        }

        state.alreadyPlayedPlayerNames = new ArrayList<>();
        if (round.getAlreadyPlayed() != null) {
            for (Player p : round.getAlreadyPlayed()) {
                if (p != null) state.alreadyPlayedPlayerNames.add(p.getName());
            }
        }

        state.currentPlayerName = currentPlayer != null ? currentPlayer.getName() : null;

        return state;
    }

    public LoadedRound toLoadedRound() {
        GameState tmpState = new GameState();
        tmpState.setPlayersData(this.playersData);
        tmpState.setRemainingCards(this.remainingCards);
        tmpState.setTrophies(this.trophies);
        tmpState.setRoundCounter(this.roundCounter);

        Game game = tmpState.toGame();

        // restore player hands
        int offset = 0;
        for (Player p : game.getPlayers()) {
            p.getHand().clear();
            if (offset + 2 <= playerHandCards.size()) {
                p.addToHand(playerHandCards.get(offset));
                p.addToHand(playerHandCards.get(offset + 1));
            }
            offset += 2;
        }

        Round round = new Round(game.getPlayers(), game.getDeck());
        Round.setRoundCounter(this.roundCounter);

        // restore offers
        for (OfferData od : offers) {
            Player owner = game.getPlayers().stream()
                    .filter(p -> p.getName().equals(od.getOwnerName()))
                    .findFirst()
                    .orElse(null);
            if (owner == null) continue;

            Offer offer = new Offer(owner, od.getFaceUpCard(), od.getFaceDownCard());
            owner.setOffer(offer);
            round.addOffer(offer);
        }

        ArrayList<Player> alreadyPlayed = new ArrayList<>();
        for (String name : alreadyPlayedPlayerNames) {
            Player p = game.getPlayers().stream().filter(pl -> pl.getName().equals(name)).findFirst().orElse(null);
            if (p != null) alreadyPlayed.add(p);
        }
        round.setAlreadyPlayed(alreadyPlayed);

        Player currentPlayer = null;
        if (currentPlayerName != null) {
            currentPlayer = game.getPlayers().stream().filter(p -> p.getName().equals(currentPlayerName)).findFirst().orElse(null);
        }

        return new LoadedRound(game, round, currentPlayer);
    }

    public static class LoadedRound {
        private final Game game;
        private final Round round;
        private final Player currentPlayer;

        public LoadedRound(Game game, Round round, Player currentPlayer) {
            this.game = game;
            this.round = round;
            this.currentPlayer = currentPlayer;
        }

        public Game getGame() {
            return game;
        }

        public Round getRound() {
            return round;
        }

        public Player getCurrentPlayer() {
            return currentPlayer;
        }
    }
}
