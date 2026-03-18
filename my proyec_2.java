package com.mycompany.c2w2;

import java.util.Scanner;


public class C2W2 {

    public static void main(String[] args) {
        Scanner teclado = new Scanner (System.in);
        
        int x[] = {1,3,10,11};
        
        int z;
        
        for(z=0; z<4; z++){
        System.out.println("Posicion" + z + "Contiene" + x[z] );
        }
    }
}