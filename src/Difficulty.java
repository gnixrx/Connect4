// Author: Ethan Widger
// Date: 6/14/2021
// Description: Describes the difficulty of the AI.

public enum Difficulty {
    EASY (-1), // Random
    MEDIUM (3), // Maximizes for the player low depth
    HARD (4); // Maximizes for the AI

    // Constructor
    private final int depth; // Symbol that is played
    Difficulty(int value) {
        this.depth = value;
    }

    // Return the Difficulty's value
    // Input: none
    // Output: value
    //
    public int value() {
        return depth;
    }

    // Get the Difficulty from an value passed
    // Input: int value you want to check
    // Output: Difficulty that matches value
    //
    public static GameInput fromValue(int value) {
        GameInput result;
        result = null;

        // For each game input test t
        for (GameInput input: GameInput.values()) {
            if (input.value() == value) {
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
