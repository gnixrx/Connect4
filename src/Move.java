// Author: Ethan Widger
// Date: 6/7/2021
// Description: Describes the move which is a set of a game input and a token played.

public class Move {
    private GameInput input;
    private Token token;

    public Move(GameInput input, Token token) {
        this.input = input;
        this.token = token;
    }

    // Gets this move's input
    // Input: None
    // Output: GameInput
    // 
    public GameInput getInput() {
        return input;
    }

    // Get this move's input's index
    // Input: None
    // Output: int index of the played game input
    // 
    public int getColumn() {
        if (input.value() >= Board.WIDTH) {
            throw new IllegalArgumentException();
        }
        return input.value();
    }

    // Gets this move's token
    // Input: None
    // Output: Token associated with the move
    // 
    public Token getToken() {
        return token;
    }

    // Gets this move's token's symbol
    // Input: None
    // Output: Char associated with this move's token
    // 
    public char getSymbol() {
        return token.symbol();
    }
}
