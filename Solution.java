import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {
/* Création de la fonction Nbchemins qui reçoit un tableau en entier, les variables M et N et déclare les variables I et J*/
    static int Chemins(int[][] tab, int M, int N, int i, int j){
        /* Rentre dans cette condition si nous sortons du tableau et renvoi 0 */
        if ((i == M || j == N) || tab[i][j] == 1){
            return 0;
        }
        /* Rentre dans cette condition lorsque nous arrivons sur une cellule et renvoi -1 pour éviter un conflit avec les 1 du tableau*/
        if (i == M-1 && j == N-1){
             return -1;
        }
        tab[i][j] = Chemins(tab, M, N,i, j+1) + Chemins(tab, M, N, i+1, j);
        return tab[i][j];

    }

    public static void main(String args[]) {
        /*Saisie du nombre de colonnes et de lignes pour créer le tableau en 2D*/
        Scanner in = new Scanner(System.in);
        int M = in.nextInt();
        int N = in.nextInt();
        int[][] tab; /*Déclaration d'un tableau de type entier*/
        tab = new int[M][N];

        if (in.hasNextLine()) {
            in.nextLine();
        }

        /*Boucle for permettant de saisir une chaine de caractère jusqu'à M fois en lignes*/
        for (int i = 0; i < M; i++) {
            String ROW = in.nextLine();
            /*Boucle for permettant de délimiter la saisie des caractères jusqu'à N fois en colonnes*/
            for (int j = 0; j < N; j++) {
                char s = ROW.charAt(j);
                tab[i][j] = Character.getNumericValue(s);

            }
        }
        /*Appel de la fonction recevant les différentes valeurs saisies*/
        Chemins(tab, M, N, 0, 0);
        /*Affichage du nombre de chemin possible entre les 2 points dans un tableau*/
        System.out.println(Math.abs(tab[0][0])); /*Utilisation de la fonction valeur absolue*/

    }
}

