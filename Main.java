import java.util.*;
public class Main {
    public static void main(String args[]){
        /*
        * Snake Game version 1.2 by jawsh bradley (2/3/23)
        * Controls are w, a, s, or d then enter to move there
        * Just press enter to keep going the same direction
        * Change the size of the game with SnakeGame(int n),
        * with a size of 2n x 2n, defaults to 6x6
        */
        SnakeGame sg = new SnakeGame(2);
        while(sg.isAlive()){
            sg.printBoard();
            // sg.printValidSpaces();
            System.out.println("Score: "+sg.getScore());
            Scanner scan = new Scanner(System.in);
            String move = scan.nextLine();
            sg.move(move);
            if(!sg.isAlive()){
                System.out.println(sg.getDeathReason());
                break;
            }
        }
    }
}
