// Author: Ethan Widger
// Date: 6/1/2021
// Description: Player tokens and symbols they use. 

public enum Token {
    EMPTY (' '),
    A ('*'),
    B ('o');

    // Constructor
    private final char symbol;
    Token (char symbol) {
        this.symbol = symbol;
    }

    // Override toString with the token symbol as a string
    @Override
    public String toString() {
        return String.valueOf(symbol);
    }

    // Return the symbol of the token
    // Input: none
    // Output: symbol of a the token
    public char symbol() {
        return symbol;
    }

    // Return winning combo 
    // Input: int number of rows
    // Output: String token symbols
    // 
    public String combo(int rows) {
        String output;
        output = "";

        for (int count = 0; count < rows; ++count) {
            output += symbol;
        }

        return output;
    }

    // Evaluate if one of the symbols match a token
    // Input: symbol you want to match to this enum
    // Output: boolean success
    //
    public static boolean evaluate(char symbol) {
        for (Token t : Token.values()) {
            if (t.symbol == symbol) {
                return true;
            }
        }
        return false;
    }

    // Return the opponent's token (opposite token)
    // Input: Token
    // Output: Token
    //
    public static Token opponent(Token token) {
        if (token.symbol() == ' ') throw new IllegalArgumentException();

        if (token.symbol() == Token.A.symbol) {
            return Token.B;
        } else {
            return Token.A;
        }
    }
}
