// Author: Ethan Widger
// Date: 6/2/2021
// Description: This class reads a file for localization and sets the strings throughout the program

import java.io.*;
import java.util.*;

public class Loc {
    private Locale locale;

    // Statements
    private static String welcome = "";
    private static String startGame;
    private static String draw;
    private static String win;
    private static String record;
    private static String goodbye;
    private static ArrayList<String> computerNames = new ArrayList<String>();

    // Prompts
    private static String askName;
    private static String askSinglePlayer;
    private static String promptMove;
    private static String playAgain;

    // Validate Responses
    private static String validateNameResponse;
    private static String validateBoardResponse;
    private static String validateYesNoResponse;

    // Error Responses
    private static String errorBoardResponse;
    private static String errorNameResponse;
    private static String errorYesNoResponse;
    private static String errorSpaceTaken;

    public Loc() throws IOException {
        this.locale = Locale.getDefault();
        readLocFile();
    }

    public Loc(Locale locale) throws IOException {
        this.locale = locale;
        readLocFile();
    }

    //#region Getters

    // Statements
    public static String str_welcome() { return welcome; }
    public static String str_startGame() { return startGame; }
    public static String str_draw() { return draw; }
    public static String str_win() { return win; }
    public static String str_record() { return record; }
    public static String str_goodbye() { return goodbye; }
    public static String str_computerName() { 
        Random randGen = new Random();
        return computerNames.get(randGen.nextInt(computerNames.size())); 
    }

    // Prompts
    public static String str_askName() { return askName; }
    public static String str_askSinglePlayer() { return askSinglePlayer; }
    public static String str_promptMove() { return promptMove; }
    public static String str_playAgain() { return playAgain; }

    // Validations
    public static String validate_NameResponse() { return validateNameResponse; }
    public static String validate_BoardResponse() { return validateBoardResponse; }
    public static String validate_YesNoResponse() { return validateYesNoResponse; }

    // Error Responses
    public static String str_errorBoardResponse() { return errorBoardResponse; }
    public static String str_errorNameResponse() { return errorNameResponse; }
    public static String str_errorYesNoResponse() { return errorYesNoResponse; }
    public static String str_errorSpaceTaken() { return errorSpaceTaken; }

    // Get locale
    public Locale getLocale() {
        return locale;
    }

    //#endregion

    // 
    public void changeLoc(Locale locale) throws IOException {
        this.locale = locale;
        readLocFile();
    }

    //
    private void readLocFile() throws IOException {
        String fileName = locale.toString() + ".txt";

        File file = new File(fileName);

        // Revert to default US_EN loc
        if (!file.exists()) {
            locale = Locale.US;
            file = new File(locale.toString() + ".txt");
        }
        
        if (!file.exists()) throw new FileNotFoundException("There are no localization files present.");

        Scanner fileRead = new Scanner(file);

        // Main file read and variable assignment
        String lastIdentifer = "";
        while (fileRead.hasNextLine()) {
            String thisLine = fileRead.nextLine();

            if (thisLine.matches("<\\w+>")) {
                lastIdentifer = thisLine;
            } else {
                switch(lastIdentifer) {
                    case "<welcome>": // Only multiline
                        welcome += thisLine + "\n";
                        break;
                    case "<startGame>":
                        startGame = thisLine;
                        break;
                    case "<draw>":
                        draw = thisLine;
                        break;
                    case "<win>":
                        win = thisLine;
                        break;
                    case "<record>":
                        record = thisLine;
                        break;
                    case "<goodbye>":
                        goodbye = thisLine;
                        break;
                    case "<computerName>":
                        computerNames.add(thisLine);
                        break;
                    case "<askName>":
                        askName = thisLine;
                        break;
                    case "<singlePlayer>":
                        askSinglePlayer = thisLine;
                        break;
                    case "<promptMove>":
                        promptMove = thisLine;
                        break;
                    case "<playAgain>":
                        playAgain = thisLine;
                        break;
                    case "<validateNameResponse>":
                        validateNameResponse = thisLine;
                        break;
                    case "<validateBoardResponse>":
                        validateBoardResponse = thisLine;
                        break;
                    case "<validateYesNoResponse>":
                        validateYesNoResponse = thisLine;
                        break;
                    case "<errorBoardResponse>":
                        errorBoardResponse = thisLine;
                        break;
                    case "<errorNameResponse>":
                        errorNameResponse = thisLine;
                        break;
                    case "<errorYesNoResponse>":
                        errorYesNoResponse = thisLine;
                        break;
                    case "<errorSpaceTaken>":
                        errorSpaceTaken = thisLine;
                        break;
                    default:
                        throw new FileNotFoundException("This localization file is not formatted properly and is invalid.");
                }
            }
            
        }

        fileRead.close();
    }
}
