// Author: Ethan Widger
// Date: 6/7/2021
// Description: Class that handles the controls are validated and proper outputs passed

import java.util.Scanner;

public class Controls {
    private Scanner input;

    public Controls() {
        input = new Scanner(System.in);
    }

    // Prompt and validations for player creation
    // Input: int player number
    // Output: String name of player
    //
    public String askValidName(int playerNum) {
        while (true) {
            // Ask for the player name
            System.out.printf(Loc.str_askName(), playerNum);

            // Look for valid inputs
            try {
                String name = input.nextLine();
                
                if (!name.matches(Loc.validate_NameResponse())) throw new IllegalArgumentException();

                return name;
            } // Otherwise do it again
            catch (Exception e) {
                System.out.print(Loc.str_errorNameResponse() + "\n");
            }
        }
    }

    // Prompt and validation for yes/no response
    // Input: Question you want to ask.
    // Output: boolean yes or no (true or false)
    //
    public boolean askValidYesNo(String question) {
        while (true) {
            System.out.print(question);

            try {
                String ask;
                String[] gameInputRegexs;

                gameInputRegexs = Loc.validate_YesNoResponse().split("\\|");

                ask = input.nextLine();

                if (!ask.matches(Loc.validate_YesNoResponse())) throw new IllegalArgumentException();

                // Check for yes, first responses
                for (int i = 0; i < (gameInputRegexs.length / 2); i++) {
                    if (ask.equalsIgnoreCase(gameInputRegexs[i])) {
                        return true;
                    }
                }
                
                return false;
            } 
            catch (Exception e) {
                System.out.print(Loc.str_errorYesNoResponse() + "\n");
            }
        }
    }

    // Prompt and validation for game inputs
    // Input: The player asking
    // Output: GameInput from the player's input.
    // 
    public GameInput askValidGameInput(Player currentPlayer) {
        while (true) {
            System.out.printf("\n" + Loc.str_promptMove(), currentPlayer.getName(), currentPlayer.getToken().symbol());

            try {
                String ask;
                String[] gameInputRegexs;
                
                gameInputRegexs = Loc.validate_BoardResponse().split("\\|");

                ask = input.nextLine();

                if (!ask.matches(Loc.validate_BoardResponse())) throw new IllegalArgumentException();
                
                // Check for quit
                for (int i = 1; i < gameInputRegexs.length - 1; i++) { // Go through non number quit strings
                    if (ask.equalsIgnoreCase(gameInputRegexs[i])) {
                        return GameInput.QUIT;
                    }
                }
                
                // Match to Gameinput Type
                return GameInput.fromValue(Integer.parseInt(ask) - 1);
            } 
            catch (Exception e) {
                System.out.print("\n" + Loc.str_errorBoardResponse());
                input.nextLine();
            }
        }
    }
}
