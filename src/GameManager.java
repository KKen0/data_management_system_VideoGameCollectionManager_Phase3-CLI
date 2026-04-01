/**
 * Kaveen Amin
 * CEN 3024C - Software Development I CRN: 23586
 * Date: April 1, 2026
 * Class: GameManager
 *
 * This class manages the collection of Game objects in the Video Game Manager
 * application. It handles loading games from a text file, saving games back to
 * the file, adding new games, removing games, searching for games, updating
 * game information, and generating reports such as the backlog report.
 */



import java.util.*;
import java.io.*;

public class GameManager {

    private List<Game> games;
    private String fileName;

    public GameManager(String fileName) {
        this.fileName = fileName;
        games = new ArrayList<>();
        loadGames();
    }

    /**
     * method: setFileName
     * purpose: updates the current file name and reloads the game data from the selected file
     * parameters: fileName - the path of the new file to load
     * return: boolean - true if the file was set and loaded successfully, otherwise false
     */
    public boolean setFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        this.fileName = fileName.trim();
        return reloadGames();
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * method: reloadGames
     * purpose: clears the current game list and reloads all game records from the current file
     * parameters: none
     * return: boolean - true if the reload was successful, otherwise false
     */
    public boolean reloadGames() {
        games.clear();
        return loadGames();
    }

    /**
     * method: ensureFileReady
     * purpose: verifies that the selected file and its parent directories exist before reading or writing
     * parameters: none
     * return: boolean - true if the file is ready for use, otherwise false
     */
    private boolean ensureFileReady() {
        try {
            File file = new File(fileName);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                return file.createNewFile();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * method: loadGames
     * purpose: reads the game data from the file and stores valid game records in memory
     * parameters: none
     * return: boolean - true if the file was loaded successfully, otherwise false
     */
    private boolean loadGames() {
        try {
            if (!ensureFileReady()) {
                return false;
            }

            File file = new File(fileName);
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] p = line.split(",");

                if (p.length != 8) {
                    continue;
                }

                Game g = new Game(
                        Integer.parseInt(p[0].trim()),
                        p[1].trim(),
                        p[2].trim(),
                        p[3].trim(),
                        Double.parseDouble(p[4].trim()),
                        Double.parseDouble(p[5].trim()),
                        Boolean.parseBoolean(p[6].trim()),
                        Integer.parseInt(p[7].trim())
                );

                games.add(g);
            }

            sc.close();
            return true;

        } catch (Exception e) {
            System.out.println("Error loading file.");
            return false;
        }
    }

    /**
     * method: saveGames
     * purpose: writes the current list of games back to the selected data file
     * parameters: none
     * return: boolean - true if the save operation was successful, otherwise false
     */
    private boolean saveGames() {
        try {
            if (!ensureFileReady()) {
                return false;
            }

            PrintWriter writer = new PrintWriter(fileName);

            for (Game g : games) {
                writer.println(g.toFileString());
            }

            writer.close();
            return true;

        } catch (Exception e) {
            System.out.println("Error saving file.");
            return false;
        }
    }

    /**
     * method: getAllGames
     * purpose: returns a copy of all games currently stored in memory
     * parameters: none
     * return: List<Game> - a list containing all stored game objects
     */
    public List<Game> getAllGames() {
        return new ArrayList<>(games);
    }

    /**
     * method: addGame
     * purpose: adds a new game to the collection if its ID does not already exist
     * parameters: game - the game object to be added
     * return: boolean - true if the game was added successfully, otherwise false
     */
    public boolean addGame(Game game) {
        if (findGame(game.getGameId()) != null) {
            return false;
        }

        games.add(game);
        saveGames();
        return true;
    }


    /**
     * method: removeGame
     * purpose: removes a game from the collection using its ID
     * parameters: id - the ID of the game to remove
     * return: boolean - true if the game was removed successfully, otherwise false
     */
    public boolean removeGame(int id) {
        Iterator<Game> iterator = games.iterator();

        while (iterator.hasNext()) {
            Game g = iterator.next();

            if (g.getGameId() == id) {
                iterator.remove();
                saveGames();
                return true;
            }
        }

        return false;
    }


    public boolean updateGameId(int currentId, int newId) {
        Game game = findGame(currentId);

        if (game == null || newId <= 0) {
            return false;
        }

        if (currentId != newId && findGame(newId) != null) {
            return false;
        }

        if (!game.updateGameId(newId)) {
            return false;
        }

        saveGames();
        return true;
    }

    public boolean updateGameTitle(int id, String newTitle) {
        Game game = findGame(id);

        if (game == null) {
            return false;
        }

        if (!game.updateTitle(newTitle)) {
            return false;
        }

        saveGames();
        return true;
    }


    public boolean updateGamePlatform(int id, String newPlatform) {
        Game game = findGame(id);

        if (game == null) {
            return false;
        }

        if (!game.updatePlatform(newPlatform)) {
            return false;
        }

        saveGames();
        return true;
    }

    public boolean updateGameGenre(int id, String newGenre) {
        Game game = findGame(id);

        if (game == null) {
            return false;
        }

        if (!game.updateGenre(newGenre)) {
            return false;
        }

        saveGames();
        return true;
    }

    public boolean updateGamePrice(int id, double newPrice) {
        Game game = findGame(id);

        if (game == null) {
            return false;
        }

        if (!game.updatePrice(newPrice)) {
            return false;
        }

        saveGames();
        return true;
    }

    public boolean updateGameHours(int id, double newHours) {
        Game game = findGame(id);

        if (game == null) {
            return false;
        }

        if (!game.updateHours(newHours)) {
            return false;
        }

        saveGames();
        return true;
    }

    public boolean updateGameCompleted(int id, boolean status) {
        Game game = findGame(id);

        if (game == null) {
            return false;
        }

        if (!game.updateCompleted(status)) {
            return false;
        }

        saveGames();
        return true;
    }

    public boolean updateGameYear(int id, int newYear) {
        Game game = findGame(id);

        if (game == null) {
            return false;
        }

        if (!game.updateYear(newYear)) {
            return false;
        }

        saveGames();
        return true;
    }

    public Game findGame(int id) {
        for (Game g : games) {
            if (g.getGameId() == id) {
                return g;
            }
        }

        return null;
    }

    /**
     * method: backlogReport
     * purpose: creates a list of all games that are not yet completed
     * parameters: none
     * return: List<Game> - a list of unfinished games
     */
    public List<Game> backlogReport() {
        List<Game> backlog = new ArrayList<>();

        for (Game g : games) {
            if (!g.isCompleted()) {
                backlog.add(g);
            }
        }

        return backlog;
    }

    /**
     * method: exportBacklog
     * purpose: exports the backlog report to the selected file path and format
     * parameters: exportPath - the destination file path, format - the export file format such as csv or txt
     * return: boolean - true if the export was successful, otherwise false
     */
    public boolean exportBacklog(String exportPath, String format) {
        List<Game> backlog = backlogReport();
        String normalizedFormat = format == null ? "" : format.trim().toLowerCase();

        try (PrintWriter writer = new PrintWriter(exportPath)) {
            if ("csv".equals(normalizedFormat)) {
                writer.println("Game ID,Title,Platform,Genre,Purchase Price,Hours Played,Completed,Release Year");
                for (Game g : backlog) {
                    writer.printf(Locale.US, "%d,%s,%s,%s,%.2f,%.2f,%b,%d%n",
                            g.getGameId(), g.getTitle(), g.getPlatform(), g.getGenre(),
                            g.getPurchasePrice(), g.getHoursPlayed(), g.isCompleted(), g.getReleaseYear());
                }
                return true;
            }

            if ("txt".equals(normalizedFormat)) {
                for (Game g : backlog) {
                    writer.println(g.toString());
                }
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
