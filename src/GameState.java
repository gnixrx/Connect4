// Author: Ethan Widger
// Date: 6/1/2021
// Description: Various states that the game can be in.

public enum GameState {
    PLAY (Token.EMPTY), // Play - Game has not ended (Default)
    TIE (Token.EMPTY), // Tie - No one won and the board is full
    WINA (Token.A), // Win A - Token A has won the game
    WINB (Token.B); // Win B - Token B has won the game

    // Constructor
    private final Token token;
    GameState (Token token) {
        this.token = token;
    }

    // Return the token of the winner (only for Winning gamestates)
    // Input: none
    // Output: symbol of a the token
    // 
    public Token token() {
        return token;
    }

    // Get Winner GameState from Token (player tokens only)
    // Input: Token
    // Output: Winning GameState which equals the Token
    // 
    public static GameState winToken(Token token) {
        GameState result = null;

        if (token == Token.EMPTY) {
            return GameState.PLAY;
        }

        for(GameState state: GameState.values()) {
            if (state.token == token) {
                result = state;
                break;
            }
        }

        if (result == null) {
            throw new IllegalArgumentException(token + " was not recognized as a unit type. ");
        }

        return result;
    }
}