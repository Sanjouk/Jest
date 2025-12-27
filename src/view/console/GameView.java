package view.console;

import model.cards.ExtensionCard; // N'oubliez pas cet import !
import model.players.Player;
import model.players.strategies.StrategyType;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GameView {

    private final Scanner scanner;

    public GameView() {
        this.scanner = new Scanner(System.in);
    }

    // Ask for number of players
    public int askNumberOfPlayers() {
        while (true) {
            System.out.print("Enter the number of players (3-4): ");
            String input = scanner.nextLine();
            try {
                int playerCount = Integer.parseInt(input);
                if (playerCount >= 3 && playerCount <= 4) {
                    return playerCount;
                } else {
                    System.out.println("Please enter a number between 3 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // Ask for a player's name
    public String askPlayerName(int playerNumber) {
        System.out.print("Enter name for player " + playerNumber + ": ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Player" + playerNumber;
        }
        return name;
    }

    // Ask for extensions to add
    public List<Integer> askForExtensions(List<ExtensionCard> availableExtensions) {
        System.out.println("\n--- MENU EXTENSIONS ---");
        System.out.println("Available extensions:");
        
        for (int i = 0; i < availableExtensions.size(); i++) {
            ExtensionCard ext = availableExtensions.get(i);
            
            // Ligne 1 : Nom et Valeur Faciale uniquement (Le bonus est expliqué dans la description)
            System.out.println((i + 1) + ". " + ext.getName() + " [Val: " + ext.getFaceValue() + "]");
            
            // Ligne 2 : Description de l'effet
            System.out.println("   Description: " + ext.getDescription());
            System.out.println(); // Ligne vide pour aérer
        }
        
        System.out.println("Enter the numbers of the extensions you want to add, separated by spaces (e.g., '1 3').");
        System.out.println("Press Enter to play without extensions.");
        System.out.print("> ");

        String input = scanner.nextLine();
        List<Integer> choices = new ArrayList<>();

        if (input.trim().isEmpty()) {
            return choices; 
        }

        System.out.println("You entered: " + input);

        String[] tokens = input.split("\\s+");
        for (String token : tokens) {
            try {
                int index = Integer.parseInt(token) - 1;
                if (index >= 0 && index < availableExtensions.size()) {
                    choices.add(index);
                } else {
                    System.out.println("Warning: " + token + " is not a valid option number (Ignored).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Warning: '" + token + "' is not a number (Ignored).");
            }
        }
        System.out.println("Extensions chosen: " + choices);
        return choices;
    }

    public void showInvalidExtensionMessage(String message) {
        System.out.println("\n========================================");
        System.out.println(" ⚠️  CONFIGURATION INVALIDE");
        System.out.println(" " + message);
        System.out.println("========================================\n");
    }

    // Ask Strategy type for VirtualPlayer
    public StrategyType askStrategy(String name) {
        System.out.println("Choose strategy for " + name + ":");
        for (StrategyType strategy : StrategyType.values()) {
            System.out.println(strategy.ordinal() + 1 + ". " + strategy);
        }

        int choice = -1;
        while (choice < 1 || choice > StrategyType.values().length) {
            System.out.print("Choose the number of the strategy: ");
            try {
                choice = scanner.nextInt();
                // IMPORTANT : Consommer le retour à la ligne restant après nextInt()
                // Sinon la prochaine méthode nextLine() (comme extensions) sera sautée.
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                scanner.nextLine(); // clear invalid input
                System.out.println("Please enter a valid number.");
            }
        }
        return StrategyType.values()[choice - 1];
    }

    // Ask for player type
    public boolean isHumanPlayer(String name) {
        while (true) {
            System.out.print("Is " + name + " a Human or Virtual player? (H/V): ");
            String type = scanner.nextLine().trim().toUpperCase();
            if (type.equals("H")) return true;
            if (type.equals("V")) return false;
            System.out.println("Please enter 'H' for Human or 'V' for Virtual.");
        }
    }

    // Show list of players
    public void showPlayers(List<Player> players) {
        System.out.println("Players added: " + players.size());
        for (Player player : players) {
            System.out.println("- " + player.getName());
        }
    }

    // Show round info
    public void showRound(int roundNumber) {
        System.out.println("\n=========================");
        System.out.println("      ROUND " + roundNumber);
        System.out.println("=========================");
    }

    // Show trophies
    public void showTrophies(String trophiesInfo) {
        System.out.println("Trophies selected: ");
        System.out.println(trophiesInfo);
    }

    // Show player score
    public void showScore(Player player) {
        System.out.println(player.getName() + " has " + player.getScore() + " points.");
    }

    // Show winner
    public void showWinner(Player winner) {
        if (winner != null) {
            System.out.println("Winner is " + winner.getName());
        } else {
            System.out.println("There is no winner");
        }
    }

    public void showWinners(List<Player> winners) {
        if (winners == null || winners.isEmpty()) {
            System.out.println("There is no winner");
        } else if (winners.size() == 1) {
            System.out.println("Winner is " + winners.get(0).getName());
        } else {
            System.out.print("It's a tie between: ");
            for (int i = 0; i < winners.size(); i++) {
                System.out.print(winners.get(i).getName());
                if (i < winners.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    public void showEndRoundMessage() {
        System.out.println("Game ended successfully.");
        System.out.println("We can assign all trophies to get the winner");
    }

    public String askSaveName() {
        System.out.print("Enter a name for this save (or press Enter for auto-generated name): ");
        return scanner.nextLine().trim();
    }
    public void showMessage(String message) {
        System.out.println(message);
    }

    public boolean askToLoadRound() {
        System.out.print("Would you like to load a saved round? (Y/N): ");
        String response = scanner.nextLine().trim().toUpperCase();
        return response.equals("Y") || response.equals("YES");
    }

    public String askSaveFileToLoad(List<String> saveFiles) {
        if (saveFiles == null || saveFiles.isEmpty()) {
            System.out.println("No saves found.");
            return null;
        }

        System.out.println("Available saves:");
        for (int i = 0; i < saveFiles.size(); i++) {
            System.out.println((i + 1) + ". " + saveFiles.get(i));
        }

        while (true) {
            System.out.print("Enter the number of the save to load (or press Enter to cancel): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;
            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < saveFiles.size()) {
                    return saveFiles.get(index);
                }
                System.out.println("Invalid choice.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public boolean askToSaveGame() {
        System.out.print("Would you like to save the game? (Y/N): ");
        String response = scanner.nextLine().trim().toUpperCase();
        return response.equals("Y") || response.equals("YES");
    }

}