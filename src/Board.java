// Author: Ethan Widger
// Date: 6/2/2021
// Description: This class handles the board, validation, and outputs.

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Board {
    static final int WIDTH = 7;
    static final int HEIGHT = 6;
    static final int CONNECT = 4;
    private char[][] board;
    private Stack<Move> moveList;

    //#region Constructors

    public Board() {
        board = emptyBoard();
        moveList = new Stack<Move>();
    }

    public Board(Board copyBoard) {
        populateBoard(copyBoard.moveList);
    }

    public Board(Stack<Move> copyMoveList) {
        populateBoard(copyMoveList);
    }

    //#endregion
    
    //#region Getters

    // Get the board
    // Input: none
    // Output: get this board's board[][]
    //
    public char[][] getBoard() {
        return board;
    }

    // Get this board's moveList
    // Input: none
    // Output: this board's ArrayList<Integer> of all the moves played in order
    // 
    public Stack<Move> getMoveList() {
        return moveList;
    }

    // Print this board status to terminal
    // Input: None
    // Output:
    // 
    //   1   2   3   4   5   6   7  
    //     |   |   |   |   |   |
    //  ---|---|---|---|---|---|---
    //     |   |   |   |   |   |
    //  ---|---|---|---|---|---|---
    //     |   |   |   |   |   |
    //  ---|---|---|---|---|---|---
    //     |   |   |   |   |   |
    //  ---|---|---|---|---|---|---
    //     |   |   | * |   |   |
    //  ---|---|---|---|---|---|---
    //     |   | * | o |   |   |
    //
    public void print() {
        final String rowSep = "---";
        final String colSep = "|";
        String boardPrint = "";
        System.out.print("\n");

        // Rows count up from the bottom because tiles get placed from the bottom and stacked up
        for (int row = HEIGHT - 1; row >= 0; row--) {

            // Create a column seperator
            if (row != HEIGHT - 1) { 
                boardPrint += " ";
                for ( int column = 0; column < WIDTH; column++ ) {
                    if (column != 0) {
                        boardPrint += colSep;
                    }
                    boardPrint += rowSep;
                }
                boardPrint += " \n";
            }

            // Create a playable row
            boardPrint += " ";
            for ( int column = 0; column < WIDTH; column++) {
                if (column != 0) {
                    boardPrint += colSep;
                }
                boardPrint += " " + board[row][column] + " ";
            }

            boardPrint += " \n";
        }
        
        // Create a column identifier for move selection
        for ( int column = 1; column < WIDTH + 1; column++ ) {
            boardPrint += "  " + column + " ";
        }

        System.out.println(boardPrint);
    }

    // Returns a list of possible valid move indexes
    // Input: none
    // Output: int[]
    //
    public ArrayList<Integer> validMoveIndexes() {
        ArrayList<Integer> validIndexes = new ArrayList<Integer>();

        for (int column = 0; column < WIDTH; column++) {
            if (board[HEIGHT - 1][column] == ' ') {
                validIndexes.add(column);
            }
        }

        return validIndexes;
    }

    //#endregion

    //#region Setters

    // Set empty board
    // Input: None
    // Output: None
    //
    private char[][] emptyBoard() {
        char[][] result = new char[HEIGHT][WIDTH];

        for ( int row = 0; row < HEIGHT; row++ ) {
            for (int column = 0; column < WIDTH; column++) {
                result[row][column] = ' ';
            }
        }
        
        return result;
    }
    
    // Populates this board with a passed move list
    // Input: move list
    // Output: boolean success
    //
    private boolean populateBoard(Stack<Move> movesToCopy) {
        board = emptyBoard();
        moveList = new Stack<Move>();
        boolean result = false;

        for (Move move: movesToCopy) {
            // If playmove is ever false something went wrong
            if (playMove(move)) {
                result = true;
            } else {
                return false;
            }
            
        }

        return result;
    }

    // Play a move
    // Input: Move which is a Game Input and Token to play
    // Output: returns if move was successful
    // 
    public boolean playMove(Move move) {

        for (int row = 0; row < HEIGHT; row++) {
            if (board[row][move.getColumn()] == ' ') {
                moveList.add(move);
                board[row][move.getColumn()] = move.getSymbol();
                return true;
            }
        }

        // The column was full or column out of range
        return false;
    }

    // Unplay a number of moves from the movelist
    // Input: depth of the removal from the latest played moves
    // Output: void, but adjusts this board's fields
    // 
    public void removeMove(int depth) {
        Stack<Move> newMoveList = new Stack<Move>();
        newMoveList.addAll(moveList);

        for (int count = 0; count < depth; count++) {
            newMoveList.pop();
        }

        populateBoard(newMoveList);
    }

    //#endregion

    //#region Win/Ranking

    // Ranking algorithms for determining board position, and how who has control over the board at given times.
    // Point rankings below:
    private final int WIN_SCORE = 1000;
    private final int OCUPPY_SCORE = 5;
    private final int POTENTIAL_SCORE = 1;

    // Evaluate the board for the GameState checking for a winner.
    // Input: none
    // Output: current GameState of the board
    //
    public GameState rankBoard() {
        GameState result = GameState.PLAY;

        // Check to see it is a tie.
        String lineCheck = "";
        for (int column = 0; column < WIDTH; column++) {
            lineCheck += board[HEIGHT-1][column];
        }
        if (lineCheck.indexOf(' ') < 0)
            return GameState.TIE;

        // Check to see if there is a win through rankBoard
        if (rankBoard(0) >= WIN_SCORE) {
            result = GameState.winToken(moveList.peek().getToken());
        }
        
        return result;
    }

    // Evaluate the board for the rank for the last move played. History allows you to rewind back to prior board states.
    // Input: int History, counting backwards from 0 to the first move played.
    // Output: A score for the board on the last move's Token - opponent's Token score.
    // 
    public int rankBoard(int history) {
        if (moveList.empty())
            return 0;

        Stack<Move> boardState = new Stack<Move>();
        boardState.addAll(moveList);

        // Remove moves to get the boardState to where we want to check
        for(int move = 0; move < history; move++) {
            // Empty boardstate is worth nothing...
            if (boardState.empty())
                return 0;
            boardState.pop();
        }

        Board checkBoard = new Board(boardState);

        int[] scores = new int[2];
        int scoreA = 0;
        int scoreB = 0;
        // Check every row from bottom to height - connect
        for (int row = 0; row <= HEIGHT - CONNECT; row++) {

            for (int column = 0; column < WIDTH; column++) {

                // Check vertical scoring
                scores = scoreLine(checkBoard, row, column, 1, 0, false);
                scoreA += scores[0];
                scoreB += scores[1];

                // Check column 0 to width - connect
                if (column <= WIDTH - CONNECT) {
                    // Check horizontal scoring
                    scores = scoreLine(checkBoard, row, column, 0, 1, false);
                    scoreA += scores[0];
                    scoreB += scores[1];

                    // Check diagonal forward scoring
                    scores = scoreLine(checkBoard, row, column, 1, 1, false);
                    scoreA += scores[0];
                    scoreB += scores[1];
                }

                // Check column connect to width
                if (column >= CONNECT - 1) {
                    // Check diagonal backward scoring
                    scores = scoreLine(checkBoard, row, column, 1, -1, false);
                    scoreA += scores[0];
                    scoreB += scores[1];
                }
            }
        }

        // A win neutralizes the other's rank regardless
        if (scoreA >= WIN_SCORE) {
            scoreB = 0;
        } else if (scoreB >= WIN_SCORE) { 
            scoreA = 0;
        }

        // Do multiplier to switch enemy team to otherside of tug of war.
        if (checkBoard.getMoveList().peek().getToken() == Token.A) {
            scoreB *= -1;
        } else {
            scoreA *= -1;
        }

        return scoreA + scoreB;
    }

    // Evaluates a line (a section of the board that is CONNECT lengths long) and returns a score set for the line
    // Input: Board the board to check the line against, 
    //        Int start row/column where to start looking, 
    //        Int rise/run to check different directions,
    //        Boolean if you want to check the last move only
    // Output: Int[] score set of score for Token.A and score for Token.B
    //
    private int[] scoreLine(Board checkBoard, int startRow, int startColumn, int rise, int run, boolean lastMoveOnly) {

        ArrayList<Character> lineCheck = new ArrayList<Character>();
        int scoreA = 0;
        int scoreB = 0;

        // Last move only (find row from column)
        int moveRow = 0;
        if (lastMoveOnly) {
            for (int row = HEIGHT - 1; row > 0; row--) {
                if (checkBoard.board[row][checkBoard.getMoveList().peek().getColumn()] == checkBoard.getMoveList().peek().getToken().symbol()) {
                    moveRow = row;
                    break;
                }
            }
        }

        boolean hasMove = false;
        for (int index = 0; index < CONNECT; index++) {
            int row = startRow + index * rise;
            int column = startColumn + index * run;

            lineCheck.add(checkBoard.board[row][column]);

            // Last move only
            if (lastMoveOnly && row == moveRow && column == checkBoard.getMoveList().peek().getColumn()) {
                hasMove = true;
            }
        }

        // Last move only
        if (lastMoveOnly && !hasMove) {
            int[] result = {0, 0};
            return result;
        }

        int countA = Collections.frequency(lineCheck, Token.A.symbol());
        int countB = Collections.frequency(lineCheck, Token.B.symbol());

        // If both players don't own territory this line
        if (!(lineCheck.contains(Token.A.symbol()) && lineCheck.contains(Token.B.symbol()))) {

            for (int i = 0; i < 2; i++) {
                int countU = 0;
                int scoreU = 0;
                if (i == 0) {
                    countU = countA;
                } else {
                    countU = countB;
                }

                switch (CONNECT - countU) {
                    case 0:
                        scoreU = WIN_SCORE;
                        break;
                    case 1:
                        scoreU = OCUPPY_SCORE * 5;
                        break;
                    case 2:
                        scoreU = OCUPPY_SCORE;
                        break;
                    case 3:
                    default:
                        scoreU = 0;
                }

                if (i == 0) {
                    scoreA = scoreU;
                } else {
                    scoreB = scoreU;
                }
            }

            // Potential territory score (places where points can easily be had)
            for (int index = 0; index < lineCheck.size(); index++) {

                if (lineCheck.get(index) == ' ') {
                    int row = (startRow + index * rise) - 1;
                    int column = startColumn + index * run;

                    if ( row < 1 ) row = 0;

                    // Row below potential is open.
                    if (checkBoard.board[row][column] != ' ') {
                        if (countA > 0) {
                            scoreA += POTENTIAL_SCORE;
                        } else if (countB > 0) {
                            scoreB += POTENTIAL_SCORE;
                        }
                    }
                }

            }

        }

        int[] result = {scoreA, scoreB};
        return result;
    }
    //#endregion
}
