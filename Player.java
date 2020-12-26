import java.awt.Point;
import java.util.Scanner;
import java.util.Vector;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

class Player {
static List<HashSet<Point>> reserved = new ArrayList<HashSet<Point>>(); 
static int N = 0; // Nombre total des joueurs (allant de 2 à 4).
static Point actP = new Point(-1, -1); // Position actuel de la moto [X1,Y1]
	
//Stock le prochain mouvement avec le nombre de chemins possibles

static class Step {
Step(String aMove, int aCount) { move = aMove; count = aCount; } 
String getMove() { return move; }
int getCount() { return count; }
@Override
public String toString() { return move + " (" + count + ")"; }
private String move; 
private int count; 
}


public static void main(String args[]) {

        // Read init information from standard input, if any
        Scanner in = new Scanner(System.in);
        Player player=new Player();
        player.initBoard();
        Random rand=new Random();

        int sum=0;
        while (true) {
        	int mX=0,mY=0;
        	// Read information from standard input
            int N = in.nextInt();
            int P= in.nextInt();
            // Compute logic here
            for(int i=1;i<=N;i++){
            	int x1=in.nextInt(),y1=in.nextInt(),x2=in.nextInt(),y2=in.nextInt();
            	player.add(x1, y1);
            	player.add(x2, y2);
            	if(P==i-1){
            		mX=x2;mY=y2;
            	}
            }

            if(player.isEmpty(mX+1, mY)){
            	System.out.println("RIGHT");
            	continue;
            }
            if(player.isEmpty(mX-1, mY)){
            	System.out.println("LEFT");
            	continue;
            }

            if(player.isEmpty(mX, mY-1)){
            	System.out.println("UP");
            	continue;
            }
            if(player.isEmpty(mX+1, mY+1)){
            	System.out.println("DOWN");
            	continue;
            }

            // System.err.println("Debug messages...");

            // Write action to standard output

        }
    }
}
