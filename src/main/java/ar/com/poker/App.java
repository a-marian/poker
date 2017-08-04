package ar.com.poker;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class App {
    
    static int sumAbsoluteDiference( int n, int matrix[][]){
        int diagonalFirst  = 0;
        int diagonalSecond = 0;
        int result = 0;
        int helper = n-1;
          for(int i=0; i < n; i++){
            for(int j=0; j < n; j++){
                if(i == j){
                   diagonalFirst = diagonalFirst + matrix[i][j];
                }
                if(j== helper){
                     diagonalSecond = diagonalSecond + matrix[i][j];
                }
            }
              helper--;
        }
        
        result = Math.abs(diagonalFirst - diagonalSecond);
        return result;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int a[][] = new int[n][n];
        for(int a_i=0; a_i < n; a_i++){
            for(int a_j=0; a_j < n; a_j++){
                a[a_i][a_j] = in.nextInt();
            }
        }
        
      int res =   sumAbsoluteDiference(n, a);
        
        System.out.println(res);
        
    }
}
