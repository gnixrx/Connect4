// Author: Ethan Widger
// Date: 6/9/2021
// Description: Driver class. Let's play Connect 4!

import java.util.Random;

public class Connect4 {
    static Player currentPlayer;
    
    private static void initCurrentPlayer(Player playerA, Player playerB) {
        playerA.setToken(Token.A);
        playerB.setToken(Token.B);

        currentPlayer = playerA;
    }

    // Main Method
    public static void main(String[] args) {
        // Load Strings
        try {
            Loc localization = new Loc();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        Board board;
        Player player1;
        Player player2;
        boolean singlePlayer;

        Input<String> name = new Input<String>(String.class, Loc.validate_NameResponse(), Loc.str_errorNameResponse());
        Input<Boolean> yn = new Input<Boolean>(Boolean.class, Loc.validate_YesNoResponse(), Loc.str_errorYesNoResponse());
        Input<GameInput> human = new Input<GameInput>(GameInput.class, Loc.validate_BoardResponse(), Loc.str_errorBoardResponse());
        Input<Difficulty> difficulty = new Input<Difficulty>(Difficulty.class, Loc.validate_DifficultyResponse(), Loc.str_errorDifficultyResponse());

        Random coinToss = new Random();
        boolean playing = true;
        
        // Welcome Message
        System.out.println(Loc.str_welcome());
        
        player1 = new Player(name.ask(String.format(Loc.str_askName(), 1)));

        singlePlayer = yn.ask(Loc.str_askSinglePlayer());

        if (singlePlayer) {
            player2 = new Computer(Loc.str_computerName(), difficulty.ask(Loc.str_askDifficulty()));
        } else {
            player2 = new Player(name.ask(String.format(Loc.str_askName(), 2)));
        }

        // Continue playing loop
        while (playing) {
            // Randomize who goes first
            if (coinToss.nextBoolean()) {
                initCurrentPlayer(player1, player2);
            } else {
                initCurrentPlayer(player2, player1);
            }

            board = new Board(); // Create a new board
            
            board.print();

            System.out.printf(Loc.str_startGame() + "\n", currentPlayer.getName());

            // Main Game Loop
            while (board.rankBoard() == GameState.PLAY && playing) {
                Move move;

                if (currentPlayer instanceof Computer) {
                    ((Computer) currentPlayer).updateBoard(board);
                    move = new Move(GameInput.fromValue(((Computer) currentPlayer).findNextMove()), currentPlayer.getToken());
                } else {
                    move = new Move(human.ask(
                                        String.format(Loc.str_promptMove(), currentPlayer.getName(), 
                                        currentPlayer.getToken().symbol())), currentPlayer.getToken()
                                    );
                }

                // The move is quit so let's exit the game loop
                if(move.getInput() == GameInput.QUIT) {
                    playing = false;
                    continue;
                }

                // Do move
                if (!board.playMove(move)) {
                    System.out.print(Loc.str_errorSpaceTaken());
                } else {
                    // Display board
                    board.print();
    
                    // Check winner
                    if (board.rankBoard() == GameState.PLAY) {
                        // Switch sides
                        if (currentPlayer == player1) {
                            currentPlayer = player2;
                        } else {
                            currentPlayer = player1;
                        }
                    }
                }

            }

            // Post Game
            if (board.rankBoard() != GameState.PLAY) {
                if (board.rankBoard() == GameState.TIE) {
                    // Display tie string
                    System.out.print("\n" + Loc.str_draw() + "\n");
                } else {
                    // Display winner string
                    System.out.printf("\n" + Loc.str_win(), currentPlayer.getName());
                    currentPlayer.addPoint();

                    // Display current records
                    System.out.printf("\n" + Loc.str_record() + "\n", player1.getName(), player1.getScore(), player2.getName(), player2.getScore());
                }

                // Ask to continue playing
                if (!yn.ask(Loc.str_playAgain())) {
                    // Exit
                    playing = false;
                    continue;
                }
            }
        }
        
        // Bye bye
        System.out.print("\n" + Loc.str_goodbye());
    }
}
