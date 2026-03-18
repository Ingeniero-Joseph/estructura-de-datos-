package vectores;

import java.util.Scanner;

public class Vectores {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        // Declaración de vectores
        int x[] = {1, 3, 10, 11};
        String nombre[] = {"juan", "luca", "maria"};

        int z;
        for (z = 0; z < 4; z++) {
            System.out.println("Posicion " + z + " Contiene " + x[z]);
        }
        System.out.println();

        for (z = 0; z < 3; z++) {
            System.out.println("Posicion " + z + " Contiene " + nombre[z]);
        }
    }
}