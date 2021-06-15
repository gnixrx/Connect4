// Author: Ethan Widger
// Date: 6/5/2021
// Description: 

public enum GameInput {
    ONE (0), 
    TWO (1), 
    THREE (2), 
    FOUR (3), 
    FIVE (4), 
    SIX (5), 
    SEVEN (6),
    QUIT (-1);

    // Constructor
    private final int index; // Symbol that is played
    GameInput(int value) {
        this.index = value;
    }

    // Return the GameInput's value
    // Input: none
    // Output: value
    //
    public int value() {
        return index;
    }
    
    // Get the GameInput from an index passed
    // Input: int index you want to check
    // Output: GameInput that matches index
    //
    public static GameInput fromValue(int index) {
        GameInput result;
        result = null;

        // For each game input test t
        for (GameInput input: GameInput.values()) {
            if (input.value() == index) {
                result = input;
                break;
            }
        }

        // Value not found
        if (result == null) {
            throw new IllegalArgumentException();
        }

        return result;
    }
}
