// Author: Ethan Widger
// Date: 6/14/2021
// Description: A class for gathering valid user inputs.

import java.util.Scanner;

public class Input<T> {
    private Class<T> c;
    private Scanner input;
    private String regex;
    private String[] regexes;
    private String error;
    
    public Input(Class<T> c, String regex, String error) {
        input = new Scanner(System.in);
        this.c = c;
        this.regex = regex;
        this.regexes = regex.split("\\|");
        this.error = error;
    }

    // Prompt and validation for inputs
    // Input: None
    // Output: GameInput from the player's input.
    // 
    public T ask(String question) {
        while (true) {
            try {
                // Ask a question
                System.out.print(question);

                String answer = input.nextLine();

                if (!answer.matches(regex)) throw new IllegalArgumentException();

                // Processing different types here to return
                if (c.isAssignableFrom(Boolean.class)) {
                    return c.cast(askBoolean(answer));
                } else if (c.isAssignableFrom(GameInput.class)) {
                    return c.cast(askGameInput(answer));
                } else if (c.isAssignableFrom(Difficulty.class)) {
                    return c.cast(askDifficulty(answer));
                } else {
                    return c.cast(answer);
                }
            } catch (Exception e) {
                System.out.print(error + "\n");
            }
        }
    }

    private Boolean askBoolean(String inputString) {
        int matchIndex = equalsIndex(inputString);
        
        // If the matched index is not found or in even index
        if (matchIndex == -1 || matchIndex % 2 == 0) {
            return false;
        }

        return true;
    }

    private GameInput askGameInput(String inputString) {
        int matchIndex = equalsIndex(inputString);

        // If matched index is above the default 0
        if (matchIndex > 0) {
            return GameInput.QUIT;
        }

        return GameInput.fromValue(Integer.parseInt(inputString) - 1);
    }

    private Difficulty askDifficulty(String inputString) {
        int matchIndex = equalsIndex(inputString);

        // 3 difficulty settings
        switch (matchIndex % 3) {
            case 2:
                return Difficulty.HARD;
            case 1:
                return Difficulty.MEDIUM;
            default:
                return Difficulty.EASY;
        }
    }

    // Find the index of the regex entered.
    private int equalsIndex(String inputString) {
        for (int i = 0; i < regexes.length; i++) {
            if (inputString.equalsIgnoreCase(regexes[i])) {
                return i;
            }
        }

        return -1;
    }
}
