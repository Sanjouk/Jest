package model.game;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages saving and loading game states to/from files.
 */
public class GameSaveManager {
    private static final String SAVE_DIRECTORY = "saves";
    private static final String FILE_EXTENSION = ".jest";

    /**
     * Saves the current game state to a file
     * @param game The game to save
     * @param currentRound The current round number
     * @param saveName Optional custom name for the save file
     * @return The path to the saved file
     * @throws IOException If saving fails
     */
    public static String saveGame(Game game, int currentRound, String saveName) throws IOException {
        // Create saves directory if it doesn't exist
        Path saveDir = Paths.get(SAVE_DIRECTORY);
        if (!Files.exists(saveDir)) {
            Files.createDirectories(saveDir);
        }

        // Generate filename
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String filename;
        if (saveName != null && !saveName.trim().isEmpty()) {
            filename = saveName.trim().replaceAll("[^a-zA-Z0-9-_]", "_") + "_" + timestamp;
        } else {
            filename = "game_" + timestamp;
        }
        filename += FILE_EXTENSION;

        Path filePath = saveDir.resolve(filename);

        // Create GameState and serialize
        GameState state = GameState.fromGame(game, currentRound);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(state);
        }

        return filePath.toString();
    }

    /**
     * Saves the game with automatic filename
     */
    public static String saveGame(Game game, int currentRound) throws IOException {
        return saveGame(game, currentRound, null);
    }

    /**
     * Loads a game state from a file
     * @param filename The name of the save file (with or without path/extension)
     * @return The loaded Game object
     * @throws IOException If loading fails
     * @throws ClassNotFoundException If deserialization fails
     */
    public static Game loadGame(String filename) throws IOException, ClassNotFoundException {
        Path filePath = resolveSaveFilePath(filename);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Save file not found: " + filePath);
        }

        GameState state;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath.toFile()))) {
            state = (GameState) ois.readObject();
        }

        return state.toGame();
    }

    /**
     * Lists all available save files
     * @return List of save file names (without path)
     */
    public static List<String> listSaveFiles() {
        List<String> saves = new ArrayList<>();
        Path saveDir = Paths.get(SAVE_DIRECTORY);

        if (!Files.exists(saveDir)) {
            return saves;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(saveDir, "*" + FILE_EXTENSION)) {
            for (Path entry : stream) {
                saves.add(entry.getFileName().toString());
            }
        } catch (IOException e) {
            System.err.println("Error listing save files: " + e.getMessage());
        }

        return saves;
    }

    /**
     * Deletes a save file
     * @param filename The name of the save file to delete
     * @return true if deleted successfully
     */
    public static boolean deleteSaveFile(String filename) {
        try {
            Path filePath = resolveSaveFilePath(filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Error deleting save file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets information about a save file
     * @param filename The save file name
     * @return SaveFileInfo object with details
     */
    public static SaveFileInfo getSaveFileInfo(String filename) throws IOException {
        Path filePath = resolveSaveFilePath(filename);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Save file not found: " + filePath);
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath.toFile()))) {
            GameState state = (GameState) ois.readObject();

            return new SaveFileInfo(
                    filename,
                    Files.getLastModifiedTime(filePath).toMillis(),
                    Files.size(filePath),
                    state.getRoundCounter(),
                    state.getPlayersData().size()
            );
        } catch (ClassNotFoundException e) {
            throw new IOException("Invalid save file format", e);
        }
    }

    /**
     * Resolves a filename to a full path
     */
    private static Path resolveSaveFilePath(String filename) {
        // If it's already a full path, use it
        Path path = Paths.get(filename);
        if (path.isAbsolute() && Files.exists(path)) {
            return path;
        }

        // Otherwise, treat it as relative to save directory
        String name = path.getFileName().toString();
        if (!name.endsWith(FILE_EXTENSION)) {
            name += FILE_EXTENSION;
        }

        return Paths.get(SAVE_DIRECTORY).resolve(name);
    }

    /**
     * Information about a save file
     */
    public static class SaveFileInfo {
        private final String filename;
        private final long lastModified;
        private final long fileSize;
        private final int roundNumber;
        private final int playerCount;

        public SaveFileInfo(String filename, long lastModified, long fileSize,
                            int roundNumber, int playerCount) {
            this.filename = filename;
            this.lastModified = lastModified;
            this.fileSize = fileSize;
            this.roundNumber = roundNumber;
            this.playerCount = playerCount;
        }

        public String getFilename() { return filename; }
        public long getLastModified() { return lastModified; }
        public long getFileSize() { return fileSize; }
        public int getRoundNumber() { return roundNumber; }
        public int getPlayerCount() { return playerCount; }

        @Override
        public String toString() {
            return String.format("%s - Round %d (%d players)",
                    filename, roundNumber, playerCount);
        }
    }
}