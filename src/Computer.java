// Author: Ethan Widger
// Date: 6/7/2021
// This class represents a computer type of player and extends the regular player class.

import java.util.ArrayList;
import java.util.Random;

public class Computer extends Player {
    Board currBoard;
    Board testBoard;
    Difficulty difficulty;
    
    // Constructor
    public Computer(String name) {
        super(name);
        currBoard = new Board();
        difficulty = Difficulty.EASY;
    }

    public Computer(String name, Difficulty difficulty) {
        super(name);
        currBoard = new Board();
        this.difficulty = difficulty;
    }

    // Sets the board object so that we can manipulate it with AI.
    // Input: board
    // Output: None
    // 
    public void updateBoard(Board board) {
        currBoard = board;
    }

    // Find the computer's next move
    // Input: none
    // Output: int from 0 - 6
    // 
    public int findNextMove() {
        Random randGen = new Random();
        int moveIndex = -1;

        if (currBoard.getMoveList().size() > 1 && difficulty != Difficulty.EASY) {

            // Create another board to manipulate
            testBoard = new Board(currBoard);
            int bestScore = Integer.MIN_VALUE;
            ArrayList<Integer> bestMoves = new ArrayList<Integer>();

            // For each possible move
            for (Integer index: testBoard.validMoveIndexes()) {
                // Play the move
                testBoard.playMove(new Move(GameInput.fromValue(index), getToken()));

                // Test the opponent play if this play is made, and the tree beyond
                int moveScore = minimax(bestScore, difficulty.value(), Integer.MIN_VALUE, Integer.MAX_VALUE, false);

                // Unplay the move
                testBoard.removeMove(1);

                if (moveScore > bestScore) {
                    bestMoves.clear();

                    bestMoves.add(index);
                    bestScore = moveScore;
                } else if (moveScore == bestScore) {
                    bestMoves.add(index);
                }
            }

            // Gather a random move from the best move list to do (since all the moves are equivilant in score)
            if (bestMoves.size() > 0) {
                moveIndex = bestMoves.get(randGen.nextInt(bestMoves.size()));
            }

            // We couldn't find a result that would lead to maximizing winning and minimize losing
            if (moveIndex == -1) moveIndex = moveFromGaussian();
        } else {
            moveIndex = moveFromGaussian();
        }

        // Return a gameinput from that int
        return moveIndex;
    }

    // Get a random normal distrubted int between 0-6 representing the move to make
    // Input: None
    // Output: Int 0-6
    // 
    private int moveFromGaussian() {
        final double STDDEV = 0.8;
        Random randGen = new Random();
        double offset = -2.0;

        // Throw away numbers out of range because nextGuassian sometimes gives you numbers way out there
        while (offset < -1.0 || offset > 1.0) {
            offset = randGen.nextGaussian() * STDDEV;
        }

        return (Board.WIDTH / 2) + (int) Math.round((Board.WIDTH / 2) * offset);
    }


    // Get walk the tree to find a good move to play now.
    // Input: board, depth, isMaximizing
    // Output: score rating for a move concidering the depth of the move.
    // 
    private int minimax(int score, int depth, int alpha, int beta, boolean isMaximizing) {
        // Bottom of tree didn't find any win or loss return last move score.
        if (depth <= 0) {
            return testBoard.rankBoard(0);
        }

        // Someone wins before depth reaches 0 return that score.
        if(testBoard.rankBoard() != GameState.PLAY) {
            int multiplier = 1;
            if (isMaximizing) { // Last move was minimizing so this is scoring for opposite team
                multiplier = -1;
            }
            int result = testBoard.rankBoard(0) * multiplier;
            return result;
        }

        // Dive further into tree
        int bestScore;
        if (isMaximizing) { // Maximizer's turn
            bestScore = Integer.MIN_VALUE;

            // For each possible move
            for (Integer validIndex: testBoard.validMoveIndexes()) {
                // Play the move
                testBoard.playMove(new Move(GameInput.fromValue(validIndex), getToken()));

                // Test the opponent play if this play is made, and the tree beyond
                bestScore = Math.max(bestScore, minimax(bestScore, depth - 1, alpha, beta, false));
                
                // Unplay the move
                testBoard.removeMove(1);

                if (bestScore >= beta) {
                    break;
                }
                alpha = Math.max(alpha, bestScore);
            }
        } else { // Minimizer's turn
            bestScore = Integer.MAX_VALUE;

            // For each possible move
            for (Integer validIndex: testBoard.validMoveIndexes()) {
                // Play the move
                testBoard.playMove(new Move(GameInput.fromValue(validIndex), Token.opponent(getToken())));

                // Test the opponent play if this play is made, and the tree beyond
                bestScore = Math.min(bestScore, minimax(bestScore, depth - 1, alpha, beta, true));
                
                // Unplay the move
                testBoard.removeMove(1);

                if (bestScore <= alpha) {
                    break;
                }
                beta = Math.min(beta, bestScore);
            }
        }
        
        return bestScore;
    }
}
