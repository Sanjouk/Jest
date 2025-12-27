package controller;

import model.players.Player;
import model.players.strategies.StrategyType;
import model.game.Round;
import view.interfaces.IGameView;
import model.game.Game;
import view.interfaces.IRoundView;
import view.ViewFactory;

import java.util.ArrayList;

public class GameController {
    private final Game model;
    private final IGameView gameView;
    private final IRoundView roundView;
    private final ViewFactory viewFactory;

    public GameController(Game model, IGameView gameView, IRoundView roundView, ViewFactory viewFactory) {
        this.model = model;
        this.gameView = gameView;
        this.roundView = roundView;
        this.viewFactory = viewFactory;
    }
//    private final GameView guiView;

    public void addPlayers(){
        int playerCount = gameView.askNumberOfPlayers();
        for (int i = 1; i <= playerCount; i++) {
            String name = gameView.askPlayerName(i);
            boolean isHuman = gameView.isHumanPlayer(name);
            if  (isHuman) {
                model.addHumanPlayer(name);
            }
            else {
                StrategyType strategy = gameView.askStrategy(name);
                model.addVirtualPlayer(name, strategy);
            }
        }
        gameView.showPlayers(model.getPlayers());
    }

    public void startGame(){
        addPlayers();
        if (model.getPlayers().size() < 3 || model.getPlayers().size() > 4) {
            throw new IllegalStateException("Jest supports 3 or 4 players only.");
        }
        model.chooseTrophies(model.getPlayers().size());
        gameView.showTrophies(model.trophiesInfo());

        playGame();

    }


    public void playGame() {
        while (!model.getDeck().isEmpty()) {
            RoundController roundController = new RoundController(new Round(model.getPlayers(), model.getDeck()), roundView, viewFactory);

            gameView.showRound(roundController.getRoundCounter());
            roundController.playRound();
            // somehow add rounds to rounds in game
//            rounds.add(currentRound);

            //change to take a deck from model not from round (reference on the same object)
            if (model.getDeck().isEmpty())
                break;
        }
        endGame();
    }

    public void endGame() {
        for (Player player : model.getPlayers()) {
            player.takeRemainingOfferCard();
        }

        gameView.showEndRoundMessage();
        model.assignTrophies();
        model.calculateAllScores();

        for (Player player : model.getPlayers()) {
            gameView.showScore(player);
        }

//        Player winner = getWinner();
//        view.showWinner(winner);
        ArrayList<Player> winners = model.getWinners();
        gameView.showWinners(winners);

    }
}
