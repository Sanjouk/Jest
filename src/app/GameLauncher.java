package app;

import controller.GameController;
import model.game.GameSaveManager;
import view.console.GameView;
import model.game.Game;

import java.util.List;

public class GameLauncher {
    public static void main(String[] args) {
        System.out.println("--- Jest Card Game ---");

        GameView view = new GameView();

        if (view.askToLoadRound()) {
            List<String> saves = GameSaveManager.listSaveFiles();
            String filename = view.askSaveFileToLoad(saves);
            if (filename != null) {
                try {
                    Game loaded = GameSaveManager.loadGame(filename);
                    GameController controller = new GameController(loaded, view);
                    controller.playGame();
                    return;
                } catch (Exception e) {
                    view.showMessage("Failed to load save: " + e.getMessage());
                }
            }
        }

        Game model = new Game();
        GameController controller = new GameController(model, view);
        controller.startGame();
    }
}
