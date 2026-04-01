/**
 * Kaveen Amin
 * CEN 3024C - Software Development I CRN: 23586
 * Date: April 1, 2026
 * Class: Game
 *
 * This class represents a video game object within the Video Game Manager
 * application. It stores information about a single game including its ID,
 * title, platform, genre, purchase price, hours played, completion status,
 * and release year. The class also provides methods to update game fields
 * and convert game data into a format suitable for saving to a text file.
 */

public class Game {

    private int gameId;
    private String title;
    private String platform;
    private String genre;
    private double purchasePrice;
    private double hoursPlayed;
    private boolean completed;
    private int releaseYear;

    /**
     * Constructor: Game
     * Purpose: Initializes a Game object with all required attributes.
     * Parameters: gameId, title, platform, genre, purchasePrice,
     *             hoursPlayed, completed, releaseYear
     * Return: none
     */
    public Game(int gameId, String title, String platform, String genre,
                double purchasePrice, double hoursPlayed,
                boolean completed, int releaseYear) {

        this.gameId = gameId;
        this.title = title;
        this.platform = platform;
        this.genre = genre;
        this.purchasePrice = purchasePrice;
        this.hoursPlayed = hoursPlayed;
        this.completed = completed;
        this.releaseYear = releaseYear;
    }

    // Getter methods return stored game information
    public int getGameId() { return gameId; }
    public String getTitle() { return title; }
    public String getPlatform() { return platform; }
    public String getGenre() { return genre; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getHoursPlayed() { return hoursPlayed; }
    public boolean isCompleted() { return completed; }
    public int getReleaseYear() { return releaseYear; }

    /**
     * Method: updateGameId
     * Purpose: Updates the ID of the game.
     * Parameter: newGameId
     * Return: boolean
     */
    public boolean updateGameId(int newGameId) {
        if(newGameId <= 0) {
            return false;
        }

        gameId = newGameId;
        return true;
    }

    /**
     * Method: updateTitle
     * Purpose: Updates the title of the game.
     * Parameter: newTitle
     * Return: boolean
     */
    public boolean updateTitle(String newTitle) {
        if(newTitle == null || newTitle.trim().isEmpty()) {
            return false;
        }

        title = newTitle.trim();
        return true;
    }

    /**
     * Method: updatePlatform
     * Purpose: Updates the platform of the game.
     * Parameter: newPlatform
     * Return: boolean
     */
    public boolean updatePlatform(String newPlatform) {
        if(newPlatform == null || newPlatform.trim().isEmpty()) {
            return false;
        }

        platform = newPlatform.trim();
        return true;
    }

    /**
     * Method: updateGenre
     * Purpose: Updates the genre of the game.
     * Parameter: newGenre
     * Return: boolean
     */
    public boolean updateGenre(String newGenre) {
        if(newGenre == null || newGenre.trim().isEmpty()) {
            return false;
        }

        genre = newGenre.trim();
        return true;
    }

    /**
     * Method: updatePrice
     * Purpose: Updates the purchase price of the game.
     * Parameter: newPrice
     * Return: boolean
     */
    public boolean updatePrice(double newPrice) {
        if(newPrice < 0) {
            return false;
        }

        purchasePrice = newPrice;
        return true;
    }

    /**
     * Method: updateHours
     * Purpose: Updates the number of hours played for the game.
     * Parameter: newHours
     * Return: boolean
     */
    public boolean updateHours(double newHours) {
        if(newHours < 0) {
            return false;
        }

        hoursPlayed = newHours;
        return true;
    }

    /**
     * Method: updateCompleted
     * Purpose: Updates the completion status of the game.
     * Parameter: status
     * Return: boolean
     */
    public boolean updateCompleted(boolean status) {
        completed = status;
        return true;
    }

    /**
     * Method: updateYear
     * Purpose: Updates the release year of the game.
     * Parameter: newYear
     * Return: boolean
     */
    public boolean updateYear(int newYear) {
        if(newYear < 1950 || newYear > 2100) {
            return false;
        }

        releaseYear = newYear;
        return true;
    }

    /**
     * Method: toFileString
     * Purpose: Converts game data into a comma-separated format for saving to file.
     * Return: String
     */
    public String toFileString() {
        return gameId + "," + title + "," + platform + "," + genre + ","
                + purchasePrice + "," + hoursPlayed + ","
                + completed + "," + releaseYear;
    }

    /**
     * Method: toString
     * Purpose: Formats the game information for display in the console.
     * Return: String
     */
    public String toString() {
        return "ID: " + gameId +
                " | Title: " + title +
                " | Platform: " + platform +
                " | Genre: " + genre +
                " | Price: $" + purchasePrice +
                " | Hours: " + hoursPlayed +
                " | Completed: " + completed +
                " | Year: " + releaseYear;
    }
}
