import java.awt.Point;
import java.util.Scanner;
import java.util.Vector;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

class Player {
    static List<HashSet<Point>> reserved = new ArrayList<HashSet<Point>>();  
    static int N = 0;                                                       // Nombre total des joueurs (allant de 2 à 4).
    static Point actP = new Point(-1, -1);                                         // Position actuel de la moto [X1,Y1]

    //Stock le prochain mouvement avec le nombre de chemins possibles
    static class Step {
        Step(String aMove, int aCount) { move = aMove; count = aCount; } 
        String  getMove()  { return move; }
        int     getCount() { return count; }
        @Override
        public String toString() { return move + " (" + count + ")"; }
        private String move;                                                
        private int count;                                                  
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String move = "LEFT"; // Direction de début
        String[] moves = new String[3];  // Ordre du prochain mouvement
        boolean firstRound = true;

        // game loop
        while (true) {
            N = in.nextInt();                                               
            int P = in.nextInt();                                           
            if (firstRound) {                                              
                for (int i = 0; i < N; ++i) {
                    reserved.add(new HashSet<Point>());
                }
            }
            for (int i = 0; i < N; i++) {
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                Point p1 = new Point(X1, Y1);
                if (firstRound) {
                    Point p0 = new Point(X0,Y0);
                    reserved.get(i).add(p0);
                    System.err.println((i == P ? "Me    " : "Player") + i + ":" + coords(p0) + " added (p0)");
                }
                if (i == P) {
                    reserved.get(i).add(p1);
                    System.err.println("Me    " + i + ":" + coords(p1) + " added");
                    actP.move(X1, Y1);                                      // Déplace la moto à la position X1, 
                } else {
                    if (p1.getX() != -1) {
                        reserved.get(i).add(p1);
                        System.err.println("Player" + i + ":" + coords(p1) + " added");
                    } else {
                        if (reserved.get(i) != null) {
                            System.err.println("Player" + i + " died. Removing its reserved locations.");
                            reserved.set(i, null);
                        }
                    }
                }
            }
            if (firstRound) { firstRound = false; }                         // Stock les coordonnées X0, Y0 seulement pour la première fois
            // Determine possible next positions to step excluding bounds
            moves = getCurlyMoves(move);                                    // Mise en place de la stratégie
            Vector<String> validMoves = getValidMoves(moves);               // Stock les mouvements possibles
            if (validMoves.isEmpty()) {
                System.err.println("Echecs...");
            } else {                                                        // Il y a au moins 1 mouvement possible
                Vector<String> safeMoves = getSafeMoves(validMoves);
                move = safeMoves.firstElement();
                System.out.println(move);
            }
        }
    } 

    // Retourne les  coordonnées de p en tant que STRING qui est utilize pour le montrer dans le debug
    private static String coords(Point p) {
        return "[x=" + (int)p.getX() + ",y=" + (int)p.getY() + "]";
    } 

    // Returns clockwise curly moving order from current move (our strategy)
    private static String[] getCurlyMoves(String move) {
        String[] moves = null;
        switch (move) {
            case "LEFT" : moves = new String[] {"UP", "LEFT", "DOWN"}; break;
            case "RIGHT": moves = new String[] {"DOWN", "RIGHT", "UP"}; break;
            case "UP"   : moves = new String[] {"RIGHT", "UP", "LEFT"}; break;
            case "DOWN" : moves = new String[] {"LEFT", "DOWN", "RIGHT"}; break;
        }
        return moves;
    } // getCurlyMoves()

    // Retourne la valeur TRUE si p est réservé
    private static boolean isReserved(Point p) {
        boolean allocated = false;
        for (int i = 0; i < N && !allocated; ++i) {
            if (reserved.get(i) != null) {
                if (reserved.get(i).contains(p)) { allocated = true; }
            }
        }
        return allocated;
    } // isReserved()
    
    // Retourne la valeur TRUE si p est dans la grille du jeu
    private static boolean onGrid(Point p) {
        int X = (int)(p.getX());
        int Y = (int)(p.getY());
        return (X >= 0 && X < 30 && Y >= 0 && Y < 20) ? true : false;
    } 

    // Retourne la position du prochain movement en tant que point
    private static Point getP(String move, Point p) {
        int X = (int)(p.getX());
        int Y = (int)(p.getY());
        switch(move) {
            case "LEFT" : return new Point(X - 1, Y);
            case "RIGHT": return new Point(X + 1, Y);
            case "UP"   : return new Point(X, Y - 1);
            case "DOWN" : return new Point(X, Y + 1);
        }
        return null;
    } 

    // Retourne un mouvement valide (s’il y en a) de mouvements, sinon retourne un vecteur vide
    private static Vector<String> getValidMoves (String[] moves) {
        Vector<String> validMoves = new Vector<>();
        for (String move : moves) {
            if (onGrid(getP(move, actP)) && !isReserved(getP(move, actP))) {
                validMoves.add(move);
            }
        }
        return validMoves;
    } 

    // Retourne les déplacements par ordre de sécurité
    private static Vector<String> getSafeMoves(Vector<String> moves) {
        Vector<Player.Step> steps = new Vector<>();     
        Vector<String> safeMoves = new Vector<>();
        for (String move : moves) {
            String[] nextMoves = getCurlyMoves(move);
            int validCount = 0;
            Point p = getP(move, actP);
            for (String m : nextMoves) {
                if (onGrid(getP(m, p)) && !isReserved(getP(m, p))) {    
                    validCount++;
                }
            }
            steps.add(new Step(move, validCount));
        }      
        for(Step step : steps) {                        // Affecte à moves 2 ou 3 directions valides à la variable safeMoves
            if (step.getCount() >= 2) {
                safeMoves.add(step.getMove());
            }
        }
        for(Step step : steps) {                        // Affecte à la variable move 1 direction valide à safeMoves
            if (step.getCount() == 1) {
                safeMoves.add(step.getMove());
            }
        }
        for(Step step : steps) {                        // Affecte à la variable move 0 direction valide à safeMoves.
            if (step.getCount() == 0) {
                safeMoves.add(step.getMove());
            }
        }
        System.err.println("Steps:");
        for (Step step : steps) {
            System.err.println(step);
        }
        if (steps.size() == 2) {                        
            int count1 = steps.get(0).getCount();
            int count2 = steps.get(1).getCount();
            if ((count1 == 1 && count2 == 3) || (count1 == 3 && count2 == 1)) {
                System.err.println("Bord détecté");
                safeMoves.clear();                      
                safeMoves.add(count1 == 1 ? steps.get(0).getMove() : steps.get(1).getMove());
            } else if ((count1 == 1 && count2 == 2) || (count1 == 2 && count2 == 1)) {
                boolean uTurn = (count1 == 1 ? isUTurn(steps.get(0).getMove()) : isUTurn(steps.get(1).getMove()));
                if (uTurn) {
                    safeMoves.clear();      
                    safeMoves.add(count1 == 1 ? steps.get(0).getMove() : steps.get(1).getMove());                
                }
            }
        }
        System.err.println("Safe moves:");
        for (int i = 0; i < safeMoves.size(); ++i) {
            System.err.println(safeMoves.get(i));
        }
        return safeMoves;
    } 
    
    // Retourne la valeur TRUE si toutes les règles définies dans “rules” correspondent
    private static boolean isRulesMatch(int[][] rules) {
        boolean match = true;
        for (int[] rule : rules) {
            Point p = new Point(rule[0], rule[1]);
            if (rule[2] == 0) {                         // Vérifie si est libre
                if (!onGrid(p) || isReserved(p)) { match = false; }
            } else {                                    // Vérifie si est occupé
                if (onGrid(p) && !isReserved(p)) { match = false; }
            }
        }
        return match;
    } 
    
    private static boolean isUTurn(String move) {
        int x = (int)(actP.getX());
        int y = (int)(actP.getY());
        boolean turn = false;
        // Règle pour détecter Uturn dans la direction d’un mouvement. Uturn est atteint si les 9 règles correspondent
        // Valeur : coordX, cordY, 0 correspond à libre et 1 à occupé
        int[][] leftRulesA  = new int[][] { {x,y+1,0}, {x-1,y+1,0}, {x-1,y,0}, {x,y-1,1}, {x-1,y-1,1},
                                            {x-2,y,1}, {x-2,y+1,1}, {x-1,y+2,1}, {x,y+2,1} };
        int[][] leftRulesB  = new int[][] { {x,y-1,0}, {x-1,y-1,0}, {x-1,y,0}, {x,y-2,1}, {x-1,y-2,1},
                                            {x-2,y-1,1}, {x-2,y,1}, {x-1,y+1,1}, {x,y+1,1} };
        int[][] rightRulesA = new int[][] { {x,y-1,0}, {x+1,y-1,0}, {x+1,y,0}, {x,y-2,1}, {x+1,y-2,1},
                                            {x+2,y-1,1}, {x+2,y,1}, {x+1,y+1,1}, {x,y+1,1} };
        int[][] rightRulesB = new int[][] { {x,y+1,0}, {x+1,y+1,0}, {x+1,y,0}, {x,y-1,1}, {x+1,y-1,1},
                                            {x+2,y,1}, {x+2,y+1,1}, {x+1,y+2,1}, {x,y+2,1} };                                            
        int[][] upRulesA    = new int[][] { {x,y-1,0}, {x+1,y,0}, {x+1,y-1,0}, {x-1,y,1}, {x-1,y-1,1},
                                            {x,y-2,1}, {x+1,y-2,1}, {x+2,y-1,1}, {x+2,y,1} };
        int[][] upRulesB    = new int[][] { {x-1,y,0}, {x-1,y-1,0}, {x,y-1,0}, {x-2,y,1}, {x-2,y-1,1},
                                            {x-1,y-2,1}, {x,y-2,1}, {x+1,y-1,1}, {x+1,y,1} };
        int[][] downRulesA  = new int[][] { {x,y+1,0}, {x+1,y,0}, {x+1,y+1,0}, {x-1,y,1}, {x-1,y+1,1},
                                            {x,y+2,1}, {x+1,y+2,1}, {x+2,y+1,1}, {x+2,y,1} };
        int[][] downRulesB  = new int[][] { {x-1,y,0}, {x-1,y+1,0}, {x,y+1,0}, {x-2,y,1}, {x-2,y+1,1},
                                            {x-1,y+2,1}, {x,y+2,1}, {x+1,y+1,1}, {x+1,y,1} };
        switch(move) {
            case "LEFT":
                if (isRulesMatch(leftRulesA) || isRulesMatch(leftRulesB)) { turn = true; }
                break;
            case "RIGHT":
                if (isRulesMatch(rightRulesA) || isRulesMatch(rightRulesB)) { turn = true; }
                break;
            case "UP":
                if (isRulesMatch(upRulesA) || isRulesMatch(upRulesB)) { turn = true; }
                break;
            case "DOWN":
                if (isRulesMatch(downRulesA) || isRulesMatch(downRulesB)) { turn = true; }
                break;
        }
        if (turn) { 
            System.err.println("UTurn est identifié pendant le mouvement " + move + "!"); 
            }
        return turn;
    }
}

