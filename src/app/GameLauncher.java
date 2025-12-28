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

 import java.util.List;
 import java.util.Scanner;

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

        Game model = selectNewOrLoadGame(mode, gameWindow);

        GameController controller = new GameController(model, gameView, roundView, viewFactory);
        controller.startGame();
    }

    private static Game selectNewOrLoadGame(GameMode mode, GameWindow gameWindow) {
        if (mode == GameMode.GUI) {
            String[] options = {"New Game", "Load Game"};
            int choice = JOptionPane.showOptionDialog(
                    gameWindow != null ? gameWindow.getFrame() : null,
                    "Start a new game or load a saved game?",
                    "Jest Card Game",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice == 1) {
                return loadGameGui(gameWindow);
            }
            return new Game();
        }

        // CONSOLE or HYBRID: keep it simple and use console prompts
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Start new game");
        System.out.println("2. Load saved game");
        System.out.print("Enter choice (1-2): ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            choice = 1;
        }

        if (choice != 2) {
            return new Game();
        }

        List<String> saves = SaveManager.listSaves();
        if (saves.isEmpty()) {
            System.out.println("No saves found in /saves. Starting a new game.");
            return new Game();
        }

        System.out.println("Available saves:");
        for (int i = 0; i < saves.size(); i++) {
            System.out.println((i + 1) + ". " + saves.get(i));
        }
        System.out.print("Select save (1-" + saves.size() + "): ");

        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            idx = 1;
        }
        if (idx < 1 || idx > saves.size()) {
            System.out.println("Invalid choice. Starting a new game.");
            return new Game();
        }

        return SaveManager.load(saves.get(idx - 1));
    }

    private static Game loadGameGui(GameWindow gameWindow) {
        List<String> saves = SaveManager.listSaves();
        if (saves.isEmpty()) {
            JOptionPane.showMessageDialog(
                    gameWindow != null ? gameWindow.getFrame() : null,
                    "No saves found in /saves. Starting a new game.",
                    "Load Game",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return new Game();
        }

        String selected = (String) JOptionPane.showInputDialog(
                gameWindow != null ? gameWindow.getFrame() : null,
                "Select a save to load:",
                "Load Game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                saves.toArray(new String[0]),
                saves.getFirst()
        );

        if (selected == null || selected.trim().isEmpty()) {
            return new Game();
        }
        return SaveManager.load(selected);
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

