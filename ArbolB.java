package arbolb;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Arbol B sencillo - Estructura de Datos II
 *
 * Se usa el "grado minimo" t = 2:
 *   - cada nodo guarda como maximo 2t - 1 = 3 claves
 *   - cada nodo tiene como maximo 2t = 4 hijos (arbol B de orden 4)
 *
 * Operaciones: insertar, buscar y mostrar el arbol por niveles.
 */
public class ArbolB {

    // ===================== NODO =====================
    static class Nodo {
        int[] claves;     // claves guardadas en el nodo
        Nodo[] hijos;     // punteros a los hijos
        int n;            // cuantas claves tiene el nodo ahora
        boolean hoja;     // true si el nodo no tiene hijos

        Nodo(int t, boolean hoja) {
            this.claves = new int[2 * t - 1];
            this.hijos = new Nodo[2 * t];
            this.n = 0;
            this.hoja = hoja;
        }
    }

    // ===================== ARBOL =====================
    private Nodo raiz;
    private final int t;

    public ArbolB(int t) {
        this.t = t;
        this.raiz = null;
    }

    // ---------- BUSCAR ----------
    public boolean buscar(int clave) {
        return buscar(raiz, clave);
    }

    private boolean buscar(Nodo nodo, int clave) {
        if (nodo == null) {
            return false;
        }
        int i = 0;
        // avanzar mientras la clave sea mayor que las del nodo
        while (i < nodo.n && clave > nodo.claves[i]) {
            i++;
        }
        // ?la encontramos en este nodo?
        if (i < nodo.n && clave == nodo.claves[i]) {
            return true;
        }
        // si es hoja y no esta, no existe
        if (nodo.hoja) {
            return false;
        }
        // bajar al hijo correspondiente
        return buscar(nodo.hijos[i], clave);
    }

    // ---------- INSERTAR ----------
    public void insertar(int clave) {
        if (buscar(clave)) {
            System.out.println("La clave " + clave + " ya existe.");
            return;
        }
        // arbol vacio: se crea la raiz
        if (raiz == null) {
            raiz = new Nodo(t, true);
            raiz.claves[0] = clave;
            raiz.n = 1;
            return;
        }
        // si la raiz esta llena, se divide y el arbol crece un nivel
        if (raiz.n == 2 * t - 1) {
            Nodo nuevaRaiz = new Nodo(t, false);
            nuevaRaiz.hijos[0] = raiz;
            dividirHijo(nuevaRaiz, 0);
            raiz = nuevaRaiz;
        }
        insertarNoLleno(raiz, clave);
    }

    /**
     * Divide el hijo "i" de "padre", que esta lleno.
     * La clave del medio SUBE al padre y el resto se reparte en dos nodos.
     */
    private void dividirHijo(Nodo padre, int i) {
        Nodo lleno = padre.hijos[i];
        Nodo derecho = new Nodo(t, lleno.hoja);
        derecho.n = t - 1;

        // copiar la mitad derecha de las claves al nuevo nodo
        for (int j = 0; j < t - 1; j++) {
            derecho.claves[j] = lleno.claves[j + t];
        }
        // copiar tambien la mitad derecha de los hijos
        if (!lleno.hoja) {
            for (int j = 0; j < t; j++) {
                derecho.hijos[j] = lleno.hijos[j + t];
            }
        }
        lleno.n = t - 1;

        // hacer espacio en el padre para el nuevo hijo
        for (int j = padre.n; j >= i + 1; j--) {
            padre.hijos[j + 1] = padre.hijos[j];
        }
        padre.hijos[i + 1] = derecho;

        // hacer espacio en el padre para la clave que sube
        for (int j = padre.n - 1; j >= i; j--) {
            padre.claves[j + 1] = padre.claves[j];
        }
        padre.claves[i] = lleno.claves[t - 1]; // la clave del medio sube
        padre.n++;
    }

    /** Inserta en un nodo que tiene espacio. */
    private void insertarNoLleno(Nodo nodo, int clave) {
        int i = nodo.n - 1;

        if (nodo.hoja) {
            // correr las claves mayores una posicion a la derecha
            while (i >= 0 && clave < nodo.claves[i]) {
                nodo.claves[i + 1] = nodo.claves[i];
                i--;
            }
            nodo.claves[i + 1] = clave;
            nodo.n++;
        } else {
            // buscar el hijo donde debe ir la clave
            while (i >= 0 && clave < nodo.claves[i]) {
                i--;
            }
            i++;
            // si ese hijo esta lleno, se divide antes de bajar
            if (nodo.hijos[i].n == 2 * t - 1) {
                dividirHijo(nodo, i);
                if (clave > nodo.claves[i]) {
                    i++;
                }
            }
            insertarNoLleno(nodo.hijos[i], clave);
        }
    }

    // ---------- MOSTRAR POR NIVELES ----------
    public void mostrar() {
        if (raiz == null) {
            System.out.println("El arbol esta vacio.");
            return;
        }
        List<Nodo> nivelActual = new ArrayList<>();
        nivelActual.add(raiz);
        int nivel = 0;

        while (!nivelActual.isEmpty()) {
            List<Nodo> siguiente = new ArrayList<>();
            StringBuilder linea = new StringBuilder("Nivel " + nivel + ": ");
            for (Nodo nodo : nivelActual) {
                linea.append("[");
                for (int j = 0; j < nodo.n; j++) {
                    linea.append(nodo.claves[j]);
                    if (j < nodo.n - 1) {
                        linea.append(" | ");
                    }
                }
                linea.append("]  ");
                if (!nodo.hoja) {
                    for (int j = 0; j <= nodo.n; j++) {
                        siguiente.add(nodo.hijos[j]);
                    }
                }
            }
            System.out.println(linea);
            nivelActual = siguiente;
            nivel++;
        }
    }

    // ===================== PROGRAMA PRINCIPAL =====================
    public static void main(String[] args) {
        ArbolB arbol = new ArbolB(2); // t = 2 -> maximo 3 claves por nodo
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== ARBOL B =====");
            System.out.println("1. Insertar clave");
            System.out.println("2. Buscar clave");
            System.out.println("3. Mostrar arbol");
            System.out.println("4. Cargar ejemplo (10, 20, 5, 6, 12, 30, 7, 17)");
            System.out.println("5. Salir");
            System.out.print("Opcion: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("Clave a insertar: ");
                    arbol.insertar(sc.nextInt());
                    arbol.mostrar();
                    break;
                case 2:
                    System.out.print("Clave a buscar: ");
                    int c = sc.nextInt();
                    if (arbol.buscar(c)) {
                        System.out.println("La clave " + c + " SI esta en el arbol.");
                    } else {
                        System.out.println("La clave " + c + " NO esta en el arbol.");
                    }
                    break;
                case 3:
                    arbol.mostrar();
                    break;
                case 4:
                    int[] ejemplo = {10, 20, 5, 6, 12, 30, 7, 17};
                    for (int x : ejemplo) {
                        arbol.insertar(x);
                    }
                    arbol.mostrar();
                    break;
                case 5:
                    System.out.println("Fin del programa.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 5);
    }
}
