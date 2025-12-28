package view.gui;

import model.cards.ExtensionCard;
import model.players.Player;
import model.players.strategies.StrategyType;
import view.interfaces.IGameView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameViewGUI implements IGameView {
    private final JFrame mainFrame;
    private final JTextArea outputArea;
    private final JPanel cardPanel;
    private String inputResult;
    private boolean waitingForInput;

    public GameViewGUI(JFrame mainFrame, JTextArea outputArea, JPanel cardPanel) {
        this.mainFrame = mainFrame;
        this.outputArea = outputArea;
        this.cardPanel = cardPanel;
        this.waitingForInput = false;
    }

    private void appendOutput(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    private String showInputDialog(String message, String title) {
        return JOptionPane.showInputDialog(mainFrame, message, title, JOptionPane.QUESTION_MESSAGE);
    }

    private int showOptionDialog(String message, String title, String[] options) {
        return JOptionPane.showOptionDialog(
            mainFrame,
            message,
            title,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
    }

    @Override
    public int askNumberOfPlayers() {
        while (true) {
            String input = showInputDialog("Enter the number of players (3-4):", "Number of Players");
            if (input == null) {
                System.exit(0);
            }
            try {
                int playerCount = Integer.parseInt(input);
                if (playerCount >= 3 && playerCount <= 4) {
                    appendOutput("Number of players: " + playerCount);
                    return playerCount;
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Please enter a number between 3 and 4.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainFrame, "Invalid input. Please enter a number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @Override
    public String askPlayerName(int playerNumber) {
        String name = showInputDialog("Enter name for player " + playerNumber + ":", "Player Name");
        if (name == null || name.trim().isEmpty()) {
            name = "Player" + playerNumber;
        }
        appendOutput("Player " + playerNumber + ": " + name);
        return name.trim();
    }

    @Override
    public StrategyType askStrategy(String name) {
        StrategyType[] strategies = StrategyType.values();
        String[] options = new String[strategies.length];
        for (int i = 0; i < strategies.length; i++) {
            options[i] = (i + 1) + ". " + strategies[i];
        }

        int choice = showOptionDialog("Choose strategy for " + name + ":", "Strategy Selection", options);
        if (choice < 0) {
            choice = 0;
        }
        appendOutput("Strategy for " + name + ": " + strategies[choice]);
        return strategies[choice];
    }

    @Override
    public boolean isHumanPlayer(String name) {
        String[] options = {"Human", "Virtual"};
        int choice = showOptionDialog("Is " + name + " a Human or Virtual player?", "Player Type", options);
        boolean isHuman = (choice == 0);
        appendOutput(name + " is a " + (isHuman ? "Human" : "Virtual") + " player");
        return isHuman;
    }

    @Override
    public void showPlayers(List<Player> players) {
        StringBuilder sb = new StringBuilder("Players added: " + players.size() + "\n");
        for (Player player : players) {
            sb.append("- ").append(player.getName()).append("\n");
        }
        appendOutput(sb.toString());
    }

    @Override
    public void showRound(int roundNumber) {
        appendOutput("\n=========================");
        appendOutput("      ROUND " + roundNumber);
        appendOutput("=========================");
    }

    @Override
    public void showTrophies(String trophiesInfo) {
        appendOutput("Trophies selected: ");
        appendOutput(trophiesInfo);
    }

    @Override
    public void showScore(Player player) {
        appendOutput(player.getName() + " has " + player.getScore() + " points.");
    }

    @Override
    public void showWinner(Player winner) {
        if (winner != null) {
            appendOutput("Winner is " + winner.getName());
        } else {
            appendOutput("There is no winner");
        }
    }

    @Override
    public void showWinners(List<Player> winners) {
        if (winners == null || winners.isEmpty()) {
            appendOutput("There is no winner");
        } else if (winners.size() == 1) {
            appendOutput("Winner is " + winners.getFirst().getName());
        } else {
            StringBuilder sb = new StringBuilder("It's a tie between: ");
            for (int i = 0; i < winners.size(); i++) {
                sb.append(winners.get(i).getName());
                if (i < winners.size() - 1) {
                    sb.append(", ");
                }
            }
            appendOutput(sb.toString());
        }
    }

    @Override
    public void showEndRoundMessage() {
        appendOutput("Game ended successfully.");
        appendOutput("We can assign all trophies to get the winner");
    }

    @Override
    public ArrayList<Integer> askForExtensions(ArrayList<ExtensionCard> availableExtensions) {
        if (availableExtensions == null || availableExtensions.isEmpty()) {
            return new ArrayList<>();
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (ExtensionCard ext : availableExtensions) {
            JCheckBox cb = new JCheckBox(ext.getName() + " [Val: " + ext.getFaceValue() + "] - " + ext.getDescription());
            checkBoxes.add(cb);
            panel.add(cb);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(640, 220));

        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                scrollPane,
                "Select Extra Cards",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        ArrayList<Integer> selected = new ArrayList<>();
        if (result != JOptionPane.OK_OPTION) {
            appendOutput("No extensions selected. Standard game.");
            return selected;
        }

        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                selected.add(i);
            }
        }

        if (selected.isEmpty()) {
            appendOutput("No extensions selected. Standard game.");
        } else {
            appendOutput("Extensions selected: " + selected);
        }

        return selected;
    }

    @Override
    public void showInvalidExtensionMessage(String message){
        JOptionPane.showMessageDialog(mainFrame, message, "Invalid Extension Configuration", JOptionPane.WARNING_MESSAGE);
        appendOutput("Invalid extension configuration: " + message);
    }

    @Override
    public boolean askSaveAfterRound() {
        String[] options = {"Save", "Don't Save"};
        int choice = showOptionDialog("Save game now?", "Save", options);
        return choice == 0;
    }

    @Override
    public String askSaveName() {
        return showInputDialog("Enter save name (optional, blank for timestamp):", "Save Name");
    }

}

