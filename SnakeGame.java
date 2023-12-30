import java.util.*;
public class SnakeGame{
    private String[][] board;
    private int n;
    private ArrayList<Integer> validSpaces;
    private HashMap<String,String> prevToDir;
    private String direction;
    private int headi;
    private int headj;
    private boolean hasApple = false;
    private String deathReason = "YOU LOSE: ";
    private boolean isAlive = true;
    private int score = 0;
    private int moves = 0;

    public SnakeGame(){
        this(3);
    }
    public SnakeGame(int n){
        // set size
        this.n = 2 * n;
        // make the board
        board = new String[this.n][this.n];
        // populate validSpaces.
        validSpaces = new ArrayList<Integer>();
        for(int i = 1; i <= this.n*this.n; i++){
            validSpaces.add(i);
        }
        // define w,a,s,d -> ^,<,v,>
        prevToDir = new HashMap<String,String>();
        prevToDir.put("w", "^");
        prevToDir.put("a", "<");
        prevToDir.put("s", "v");
        prevToDir.put("d", ">");
        // spawn snake
        spawnSnake();
        // spawn an apple
        spawnApple();
    }
    //move the snake up down left or right
    public void move(String move){
        // update moves
        moves++;
        // make a space at current head location
        board[headi][headj] = " ";
        // remember space location for the body
        int spacei = headi;
        int spacej = headj;
        // remember where head is supposed to go
        switch(move){
            case "w":
                if(headi-1 < 0){
                    isAlive = false;
                    deathReason+=": Hit the bounds";
                    return;
                }
                direction = "w";
                headi -= 1;
                break;
            case "a":
                if(headj-1 < 0){
                    isAlive = false;
                    deathReason+=": Hit the bounds";
                    return;
                }
                direction = "a";
                headj-=1;
                break;
            case "s":
                if(headi+1 >= n){
                    isAlive = false;
                    deathReason+=": Hit the bounds";
                    return;
                }
                direction = "s";
                headi+=1;
                break;
            case "d":
                if(headj+1 >= n){
                    isAlive = false;
                    deathReason+=": Hit the bounds";
                    return;
                }
                direction = "d";
                headj+=1;
                break;
            default:
                //move in direction otherwise...
                move(direction);
                return;
        }
        System.out.println(board[headi][headj]);
        // move the body
        moveBody(spacei, spacej, direction);
        System.out.println("moved the body!");
        // if the head is obstructing anything, we lost, if not, put the head there
        System.out.println(board[headi][headj]);
        if(board[headi][headj] == null){
            board[headi][headj] = "O";
            validSpaces.remove(Integer.valueOf(unraveledIndex(headi,headj)));
            
            // if the head is on top of an apple, then we have an apple!
        } else if(board[headi][headj] == "@") {
            board[headi][headj] = "O";
            validSpaces.remove(Integer.valueOf(unraveledIndex(headi,headj)));
            hasApple=true;
            score++;
            // spawn another apple
            spawnApple();
        } else{
            isAlive = false;
            deathReason += "Hit head on "+board[headi][headj]+" at ("+headi+","+headj+")";
            return;
        }
    }
    // move the body
    private void moveBody(int i, int j, String prev){
        // if we have an apple, simply extend
        if(hasApple){
            hasApple = false;
            board[i][j] = prevToDir.get(direction);
            validSpaces.remove(Integer.valueOf(unraveledIndex(i, j)));
            return;
        }
        // if we're at the head, stop!
        if(headi == i && headj == j){
            board[i][j] = null;
            return;
        }
        // if not... check up
        if(i-1 > -1 && board[i-1][j] == "v"){
            board[i][j] = prevToDir.get(prev);
            validSpaces.remove(Integer.valueOf(unraveledIndex(i,j)));
            moveBody(i-1, j, "s");
            return;
        }
        //check left
        if(j-1 > -1 && board[i][j-1] == ">"){
            board[i][j] = prevToDir.get(prev);
            validSpaces.remove(Integer.valueOf(unraveledIndex(i,j)));
            moveBody(i,j-1, "d");
            return;
        }
        //check down
        if(i+1 < n && board[i+1][j] == "^"){
            board[i][j] = prevToDir.get(prev);
            validSpaces.remove(Integer.valueOf(unraveledIndex(i,j)));
            moveBody(i+1, j, "w");
            return;
        }
        //check right
        if(j+1 < n && board[i][j+1] == "<"){
            board[i][j] = prevToDir.get(prev);
            validSpaces.remove(Integer.valueOf(unraveledIndex(i,j)));
            moveBody(i,j+1, "a");
            return;
        }
        //if nothing is pointing then put nothing because we've reached the tail
        board[i][j] = null;
        validSpaces.add(unraveledIndex(i,j));
    }
    // spawn a snake
    private void spawnSnake(){
        board[0][0] = "O";
        validSpaces.remove(Integer.valueOf(1));
        direction = "s";
    }
    // spawn an apple on a valid space
    private void spawnApple(){
        if(score == n*n){
            isAlive = false;
            deathReason = "YOU WIN: (moves: "+moves+")";
            return;
        }
        Random r = new Random();
        int index = r.nextInt(validSpaces.size());
        int appleLocation = validSpaces.get(index);
        //System.out.println("Spawning apple at "+appleLocation);
        validSpaces.remove(index);
        int i = (appleLocation-1)/n;
        int j = appleLocation-1-(n*i);
        board[i][j] = "@";

    }
    // return an unraveled index of i,j
    private int unraveledIndex(int i, int j){
        return n*i+j+1;
    }
    // return if we're isAlive or not
    public boolean isAlive(){
        return isAlive;
    }
    public String getDeathReason(){
        return deathReason;
    }
    public int getScore(){
        return score;
    }
    // print out the board
    public void printBoard(){
        for(int i = 0; i < n; i++){
            for(int j = 0; j< n;j++){
                if(board[i][j] == null){
                    System.out.print(".");
                }else{
                    System.out.print(board[i][j]);
                }
            }
            System.out.println();
        }
    }
    public void printValidSpaces(){
        System.out.print("Valid Spaces:");
        for(int space : validSpaces){
            System.out.print(space+",");
        }
        System.out.println();
    }
}