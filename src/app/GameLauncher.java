package app;

import controller.GameController;
import model.game.Game;
import view.ViewFactory;
import view.console.GameView;
import view.console.RoundView;
import view.gui.GameViewGUI;
import view.gui.GameWindow;
import view.gui.RoundViewGUI;
import view.hybrid.GameViewHybrid;
import view.hybrid.RoundViewHybrid;
import view.interfaces.IGameView;
import view.interfaces.IRoundView;

import javax.swing.*;

public class GameLauncher {
    public enum GameMode {
        CONSOLE, GUI, HYBRID
    }

    public static void main(String[] args) {
        GameMode mode = selectMode();
        
        System.out.println("--- Jest Card Game ---");
        if (mode == GameMode.GUI || mode == GameMode.HYBRID) {
            System.out.println("Starting in " + mode + " mode...");
        }

        Game model = new Game();
        IGameView gameView;
        IRoundView roundView;
        ViewFactory viewFactory;
        GameWindow gameWindow = null;

        switch (mode) {
            case CONSOLE:
                gameView = new GameView();
                roundView = new RoundView();
                viewFactory = new ViewFactory(ViewFactory.ViewMode.CONSOLE);
                break;
            case GUI:
                gameWindow = new GameWindow();
                gameWindow.show();
                gameView = new GameViewGUI(gameWindow.getFrame(), gameWindow.getOutputArea(), gameWindow.getCardPanel());
                roundView = new RoundViewGUI(gameWindow.getOutputArea());
                viewFactory = new ViewFactory(ViewFactory.ViewMode.GUI, 
                                             gameWindow.getFrame(), 
                                             gameWindow.getOutputArea(), 
                                             gameWindow.getCardPanel(),
                                             gameWindow.getHandPanel(),
                                             gameWindow.getOffersPanel());
                break;
            case HYBRID:
                gameWindow = new GameWindow();
                gameWindow.show();
                GameView consoleGameView = new GameView();
                GameViewGUI guiGameView = new GameViewGUI(gameWindow.getFrame(), gameWindow.getOutputArea(), gameWindow.getCardPanel());
                gameView = new GameViewHybrid(consoleGameView, guiGameView);
                RoundView consoleRoundView = new RoundView();
                RoundViewGUI guiRoundView = new RoundViewGUI(gameWindow.getOutputArea());
                roundView = new RoundViewHybrid(consoleRoundView, guiRoundView);
                viewFactory = new ViewFactory(ViewFactory.ViewMode.HYBRID, 
                                             gameWindow.getFrame(), 
                                             gameWindow.getOutputArea(), 
                                             gameWindow.getCardPanel(),
                                             gameWindow.getHandPanel(),
                                             gameWindow.getOffersPanel());
                break;
            default:
                gameView = new GameView();
                roundView = new RoundView();
                viewFactory = new ViewFactory(ViewFactory.ViewMode.CONSOLE);
        }

        GameController controller = new GameController(model, gameView, roundView, viewFactory);
        controller.startGame();
    }

    private static GameMode selectMode() {
        if (System.console() == null) {
            // No console available, use GUI mode selection
            String[] options = {"Console", "GUI", "Hybrid"};
            int choice = JOptionPane.showOptionDialog(
                null,
                "Select game mode:",
                "Jest Card Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            return switch (choice) {
                case 0 -> GameMode.CONSOLE;
                case 1 -> GameMode.GUI;
                case 2 -> GameMode.HYBRID;
                default -> GameMode.CONSOLE;
            };
        } else {
            // Console available, ask via console
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            System.out.println("Select game mode:");
            System.out.println("1. Console");
            System.out.println("2. GUI");
            System.out.println("3. Hybrid (Console + GUI)");
            System.out.print("Enter choice (1-3): ");
            
            int choice = scanner.nextInt();
            return switch (choice) {
                case 1 -> GameMode.CONSOLE;
                case 2 -> GameMode.GUI;
                case 3 -> GameMode.HYBRID;
                default -> {
                    System.out.println("Invalid choice, defaulting to Console mode");
                    yield GameMode.CONSOLE;
                }
            };
        }
    }
}

