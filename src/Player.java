// Author: Ethan Widger
// Date: 6/6/2021
// Description: Represents the players in the game, their token, and their current game wins.

public class Player {
    private String name;
    private int score;
    private Token token;

    // Constructor
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    // Name retrieval
    public String getName() {
        return name;
    }

    // Score setting and retrieval
    // Input: None
    // Output: Score
    //
    public void addPoint() {
        score++;
    }

    public int getScore() {
        return score;
    }

    // Token setting and retrieval
    // Input: Token
    // Output: Token
    //
    public void setToken(Token token) {
        this.token = token;
    }
    public Token getToken() {
        return token;
    }
}
