import java.util.*;
import java.io.*;
import java.math.*;

class Player {
	int[][] board;
	public void initBoard(){
		board=new int[30][20];
		for(int i=0;i<30;i++){
			for(int j=0;j<20;j++){
				board[i][j]=0;
			}
		}
	}
	public void add(int x,int y){
		board[x][y]=1;
	}
	public boolean isEmpty(int x,int y){
		if(x>=29||x<0||y>=19||y<0){
			return false;
		}
		if(board[x][y]==0){
			return true;
		}
		return false;
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
