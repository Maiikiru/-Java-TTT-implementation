package TTTGame;

import TTTAI.TTTAI;

import java.util.Random;
import java.util.Scanner;

public class TTTGame {
    //Constants will be 3 by default.
    //todo if you want to make the game work for bigger than 4 boards, must fix all of the winning methods.
    public final int BOARD_HEIGHT = 3;
    public final int BOARD_LENGTH = 3;


    //todo remove this later.
    public long numXWin = 0;
    public long numDraw = 0;
    public long numOWin = 0;

    private boolean xTurn;
    private int numPiecesDown;
    private final char[][] tttBoard;
    //this variable is for seeing the current game state of the game. 0 = normal, 1 = X wins, 2 = O wins, 3 = Draw.
    private int gameState;

    //Default constructor
    public TTTGame(){
        tttBoard = new char[BOARD_HEIGHT][BOARD_LENGTH];
        numPiecesDown = 0;
        xTurn = true;
        gameState = 0;
    }

    int numGames = 0;
    boolean threadRunning = true;
    public void startMultiThreaded(){
        Thread games = new Thread(()->{
            while(threadRunning){
                System.out.println("Num games played:\t\t\t\t"+numGames);
                try{
                    Thread.sleep(5000);
                }catch (Exception e){}
            }
        });
        games.start();
        Thread ttt = new Thread(()->{
            Random r = new Random();
            while(threadRunning){
                while(gameState==0){
                    placePiece(r.nextInt(3),r.nextInt(3));
                }
                numGames++;
                resetGame();
            }
        });
        ttt.start();
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        threadRunning = false;
        ttt.interrupt();
        System.out.println("Num X Win:\t\t\t\t\t\t"+numXWin);
        System.out.println("Num O Win:\t\t\t\t\t\t"+numOWin);
        System.out.println("Num Draw:\t\t\t\t\t\t"+numDraw);

        System.out.println("\nPercentage X Win:\t\t\t\t"+((double)numXWin/(numXWin+numOWin+numDraw)) * 100);
        System.out.println("Percentage O Win:\t\t\t\t"+(double)numOWin/(numXWin+numOWin+numDraw) * 100);
        System.out.println("Percentage draw:\t\t\t\t"+(double)numDraw/(numXWin+numOWin+numDraw) * 100);

    }


    /**
     * This method will return an int array with the first index being the row position the user wants and the second
     * index being the column position.
     * @param rawPos the raw position from 0-8 (or more if more than 3x3).
     * @return an integer array containing the row position and column position from the user choice.
     */
    private int[] getPositionRowAndCol(int rawPos){
        int[] positions = new int[2];
        positions[0]= rawPos/3;
        positions[1] = rawPos%3;
        return positions;
    }

    /**
     * This method will say who's turn it currently is.
     */
    private void sayWhoseTurn(){
        if(xTurn){
            System.out.println("X, it is your turn.");
        }else{
            System.out.println("O, it is your turn.");
        }
    }

    /**
     * This method gets and checks the user input before returning it back.
     * Note that the grid the user chooses will range from 0-8 with 8 being the bottom right corner.
     * @param sc the scanner that is used to get user input
     * @return the user input value that has been checked.
     */
    private int getCheckUserInput(Scanner sc){
        do{
            System.out.println("Please decide your move.");
            try{
                int userInput = Integer.parseInt(sc.next());
                if(userInput >= 0 && userInput < BOARD_HEIGHT * BOARD_LENGTH){
                    return userInput;
                }else{
                    System.out.println("Please enter a valid choice (0-"+(BOARD_HEIGHT*BOARD_LENGTH -1)+")");
                }
            }catch (NumberFormatException e){
                System.out.println("Please enter a number");
            }
        }while(true);
    }

    /**
     * This method resets the game.
     */
    public void resetGame(){
        xTurn = true;
        numPiecesDown = 0;
        if(gameState == 1){
            numXWin++;
        }else if(gameState ==2){
            numOWin++;
        }else if(gameState ==3){
            numDraw++;
        }
        gameState = 0;
        for(int i=0;i<BOARD_HEIGHT;i++){
            for(int j=0;j<BOARD_LENGTH;j++){
                tttBoard[i][j] = '\u0000';
            }
        }
    }


    /**
     * This method will attempt to place a piece at the specified position.
     * This method will return true if successful, and false if not successful.
     * @param rowPos the row position that you want to place the piece.
     * @param colPos the column position that you want to place the piece.
     * @return true if the piece was able to be successfully placed, false otherwise.
     */
    public boolean placePiece(int rowPos, int colPos){
        if(!spotAvailable(rowPos,colPos)) return false;
        else{
            if(xTurn){
                tttBoard[rowPos][colPos] = 'X';
            }else{
                tttBoard[rowPos][colPos] = 'O';
            }
            numPiecesDown++;
            incrementTurn();
            updateGameState();
            return true;
        }
    }

    /**
     * This method will check if a spot is still available by checking if that spot contains null.
     * Returns true if it is null, otherwise false.
     * @param rowPos the row position that needs to be checked.
     * @param colPos the column position that needs to be checked.
     * @return true if it's available, false if not.
     */
    public boolean spotAvailable(int rowPos, int colPos){
        return tttBoard[rowPos][colPos] == '\u0000';
    }

    /**
     * This method changes which player's turn it is.
     * If it's x's turn then it will now be O's turn and vice versa.
     */
    private void incrementTurn(){
        xTurn = !xTurn;
    }

    /**
     * This method will print the board for the user to see.
     */
    public void printBoard(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< BOARD_HEIGHT; i++){
            sb.append("|");
            for(int j = 0; j< BOARD_LENGTH; j++){
                char currChar = tttBoard[i][j];
                if(currChar=='\u0000'){
                    sb.append(i*3+j).append("|");
                }else{
                    sb.append(currChar).append("|");
                }

            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    /**
     * This method will check the game status and will change it by checking if there is a change in how the game is.
     * This method will return true if the game state has been changed.
     * @return true if the game status has changed from 0.
     */
    public boolean updateGameState(){
        if(numPiecesDown >= 9){
            gameState = 3;
            return true;
        }
        return (hasWinnerRow() || hasWinnerColumn() || hasWinnerDiagonal());
    }

    /**
     * This method will not change the game state, but will rather just return the current game state.
     * @return the current game state.
     */
    //todo fix this.
    public int getCurrentGameState(){
        if(numPiecesDown >= 9){
            return 3;
        }
        int oldGameState = gameState;
        hasWinnerRow();
        hasWinnerColumn();
        hasWinnerDiagonal();
        int currentGameState = gameState;
        gameState = oldGameState;
        return currentGameState;
    }

    /**
     * This method will check all the rows for a win.
     * @return true if one side has won, false if not.
     */
    private boolean hasWinnerRow(){
        int numInARow = 1;
        for(int i=0;i<BOARD_HEIGHT;i++){
            for(int j=0;j<BOARD_LENGTH-1;j++){
                if(tttBoard[i][j] == '\u0000') break;

                if(tttBoard[i][j]==tttBoard[i][j+1]) numInARow++;
                else break;
            }
            if(numInARow == 3){
                if(tttBoard[i][2]=='X') gameState = 1;
                else if(tttBoard[i][2] == 'O') gameState = 2;
                else throw new RuntimeException("Error in hasWinnerRow");
                return true;
            }
            else numInARow = 1;
        }
        return false;
    }

    /**
     * This method will check all the columns for a win.
     * @return true if one side has won, false if not.
     */
    private boolean hasWinnerColumn(){
        int numInARow = 1;
        for(int i=0;i<BOARD_LENGTH;i++){
            for(int j=0;j<BOARD_HEIGHT-1;j++){
                if(tttBoard[j][i] == '\u0000') break;

                if((tttBoard[j][i]==tttBoard[j+1][i])) numInARow++;
                else break;
            }
            if(numInARow == 3){
                if(tttBoard[2][i]=='X') gameState = 1;
                else if(tttBoard[2][i] == 'O') gameState = 2;
                else throw new RuntimeException("Error in hasWinnerCol");
                return true;
            }
            else numInARow = 1;
        }
        return false;
    }
    /**
     * This method will check all the diagonals for a win.
     * @return true if one side has won, false if not.
     */
    //todo if you make this game bigger ie more than 3x3, you need to fix this.
    private boolean hasWinnerDiagonal(){
        if((tttBoard[1][1] != '\u0000') && (tttBoard[0][2]==tttBoard[1][1] && tttBoard[1][1] == tttBoard[2][0])
                || ((tttBoard[1][1] != '\u0000') &&(tttBoard[0][0]==tttBoard[1][1] && tttBoard[1][1] == tttBoard[2][2]))){
            if(tttBoard[1][1]=='X')gameState = 1;
            else if(tttBoard[1][1]=='O') gameState = 2;
            return true;
        }
        return false;
    }

    /**
     * This method will return a pointer to the Tic-Tac-Toe Board
     * @return a pointer to the board.
     */
    public char[][] getBoard(){
        return tttBoard;
    }

    /**
     * This method gets the current game state.
     * 0 = normal, 1 = X wins, 2 = O wins, 3 = Draw.
     * @return the game state.
     */
    public int getGameState() {
        return gameState;
    }
}
