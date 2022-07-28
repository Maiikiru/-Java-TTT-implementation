package TTTGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TTTGameTest {

    @Test
    public void testXWinDiagonal(){
        TTTGame tttGame = new TTTGame();
        tttGame.placePiece(0,0);
        tttGame.placePiece(0,1);
        tttGame.placePiece(1,1);
        tttGame.placePiece(1,2);
        tttGame.placePiece(2,2);
        Assertions.assertEquals(1, tttGame.getGameState());
    }

    @Test
    public void testOWinDiagonal(){
        TTTGame tttGame = new TTTGame();
        tttGame.placePiece(0,1);
        tttGame.placePiece(0,0);
        tttGame.placePiece(1,2);
        tttGame.placePiece(1,1);
        tttGame.placePiece(2,1);
        tttGame.placePiece(2,2);
        Assertions.assertEquals(2, tttGame.getGameState());
    }

    @Test
    public void testEmpty(){
        TTTGame tttGame = new TTTGame();
        Assertions.assertEquals(0,tttGame.getGameState());
    }

    //todo finish
    @Test
    public void testFullBoard(){
        TTTGame tttGame = new TTTGame();

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){

            }
        }
        tttGame.printBoard();

    }



}