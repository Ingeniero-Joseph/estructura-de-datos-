package arbolb;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// REGISTRO DE ESTUDIANTES usando un ARBOL B.
// Los estudiantes se organizan por codigo. Cada nodo guarda maximo 3 estudiantes.
// Cuando un nodo se llena, se divide y el codigo del medio sube.
// Asi el arbol siempre queda balanceado y buscar es muy rapido.
public class ArbolB {

    // ESTUDIANTE: los datos que se guardan
    static class Estudiante {
        int codigo;
        String nombre;
        String carrera;

        Estudiante(int codigo, String nombre, String carrera) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.carrera = carrera;
        }

        public String toString() {
            return codigo + " - " + nombre + " (" + carrera + ")";
        }
    }

    // NODO: la cajita del arbol, guarda hasta 3 estudiantes
    static class Nodo {
        Estudiante[] datos;   // estudiantes guardados (maximo 3), ordenados por codigo
        Nodo[] hijos;         // hijos del nodo (maximo 4)
        int n;                // cuantos estudiantes tiene ahora
        boolean hoja;         // true si no tiene hijos
        int nivel;            // nivel en el arbol (0 = raiz)
        int inicio;           // columna para dibujarlo
        int centro;           // centro para dibujar las ramas

        Nodo(int t, boolean hoja) {
            this.datos = new Estudiante[2 * t - 1];   // 3 espacios
            this.hijos = new Nodo[2 * t];             // 4 espacios
            this.n = 0;
            this.hoja = hoja;
        }
    }

    // ARBOL: solo guarda la raiz; desde ella se llega a todo
    private Nodo raiz;
    private final int t;   // t = 2
    private int total = 0; // cuantos estudiantes hay registrados

    public ArbolB(int t) {
        this.t = t;
        this.raiz = null;
    }

    // BUSCAR: compara el codigo con los del nodo y baja por el hijo correcto
    // Devuelve el estudiante, o null si no existe
    public Estudiante buscar(int codigo) {
        Nodo nodo = raiz;
        int pasos = 0;
        while (nodo != null) {
            pasos++;
            int i = 0;
            while (i < nodo.n && codigo > nodo.datos[i].codigo) {
                i++;
            }
            if (i < nodo.n && codigo == nodo.datos[i].codigo) {
                ultimosPasos = pasos;
                return nodo.datos[i];             // lo encontro
            }
            if (nodo.hoja) {
                break;                            // llego a una hoja y no esta
            }
            nodo = nodo.hijos[i];                 // baja al hijo
        }
        ultimosPasos = pasos;
        return null;
    }

    private int ultimosPasos = 0;   // nodos revisados en la ultima busqueda

    // REGISTRAR: mete un estudiante nuevo en el arbol
    public void registrar(Estudiante e, boolean avisos) {
        if (buscar(e.codigo) != null) {
            System.out.println("Ya existe un estudiante con el codigo " + e.codigo + ".");
            return;
        }
        // arbol vacio: el estudiante queda en la raiz
        if (raiz == null) {
            raiz = new Nodo(t, true);
            raiz.datos[0] = e;
            raiz.n = 1;
            total++;
            return;
        }
        // raiz llena: se divide y el arbol crece un nivel
        if (raiz.n == 2 * t - 1) {
            Nodo nuevaRaiz = new Nodo(t, false);
            nuevaRaiz.hijos[0] = raiz;
            dividirHijo(nuevaRaiz, 0, avisos);
            raiz = nuevaRaiz;
        }
        insertarNoLleno(raiz, e, avisos);
        total++;
    }

    // DIVIDIR: parte un nodo lleno en dos y el codigo del medio sube al padre
    private void dividirHijo(Nodo padre, int i, boolean avisos) {
        Nodo lleno = padre.hijos[i];
        if (avisos) {
            System.out.println("  >> El nodo " + texto(lleno) + " esta lleno: se divide y sube el "
                    + lleno.datos[t - 1].codigo);
        }
        Nodo derecho = new Nodo(t, lleno.hoja);
        derecho.n = t - 1;

        // pasa la mitad derecha al nodo nuevo
        for (int j = 0; j < t - 1; j++) {
            derecho.datos[j] = lleno.datos[j + t];
        }
        if (!lleno.hoja) {
            for (int j = 0; j < t; j++) {
                derecho.hijos[j] = lleno.hijos[j + t];
            }
        }
        lleno.n = t - 1;

        // hace espacio en el padre para el nodo nuevo
        for (int j = padre.n; j >= i + 1; j--) {
            padre.hijos[j + 1] = padre.hijos[j];
        }
        padre.hijos[i + 1] = derecho;

        // sube el estudiante del medio al padre
        for (int j = padre.n - 1; j >= i; j--) {
            padre.datos[j + 1] = padre.datos[j];
        }
        padre.datos[i] = lleno.datos[t - 1];
        padre.n++;
    }

    // INSERTAR EN NODO CON ESPACIO: baja hasta la hoja y pone el estudiante en orden
    private void insertarNoLleno(Nodo nodo, Estudiante e, boolean avisos) {
        int i = nodo.n - 1;

        if (nodo.hoja) {
            // corre los codigos mayores y mete el nuevo en su lugar
            while (i >= 0 && e.codigo < nodo.datos[i].codigo) {
                nodo.datos[i + 1] = nodo.datos[i];
                i--;
            }
            nodo.datos[i + 1] = e;
            nodo.n++;
        } else {
            // busca por que hijo bajar
            while (i >= 0 && e.codigo < nodo.datos[i].codigo) {
                i--;
            }
            i++;
            // si ese hijo esta lleno, lo divide antes de bajar
            if (nodo.hijos[i].n == 2 * t - 1) {
                dividirHijo(nodo, i, avisos);
                if (e.codigo > nodo.datos[i].codigo) {
                    i++;
                }
            }
            insertarNoLleno(nodo.hijos[i], e, avisos);
        }
    }

    // LISTAR: muestra todos los estudiantes ordenados por codigo
    // Recorre el arbol en orden: hijo izquierdo, dato, siguiente hijo...
    public void listar() {
        if (raiz == null) {
            System.out.println("No hay estudiantes registrados.\n");
            return;
        }
        System.out.println("\nEstudiantes registrados (" + total + "), ordenados por codigo:");
        listarEnOrden(raiz);
        System.out.println();
    }

    private void listarEnOrden(Nodo nodo) {
        for (int i = 0; i < nodo.n; i++) {
            if (!nodo.hoja) {
                listarEnOrden(nodo.hijos[i]);    // primero los menores
            }
            System.out.println("  " + nodo.datos[i]);
        }
        if (!nodo.hoja) {
            listarEnOrden(nodo.hijos[nodo.n]);   // al final los mayores
        }
    }

    // DIBUJAR: muestra el arbol de codigos con ramas / | \ y sus niveles

    // convierte un nodo en texto con sus codigos, por ejemplo "[1005 | 1012]"
    private String texto(Nodo nodo) {
        String separador = nodo.hoja ? "|" : " | ";
        String s = "[";
        for (int j = 0; j < nodo.n; j++) {
            s += nodo.datos[j].codigo;
            if (j < nodo.n - 1) {
                s += separador;
            }
        }
        return s + "]";
    }

    // guarda los nodos en una lista, nivel por nivel
    private List<Nodo> nodosPorNivel() {
        List<Nodo> lista = new ArrayList<>();
        raiz.nivel = 0;
        lista.add(raiz);
        for (int i = 0; i < lista.size(); i++) {
            Nodo actual = lista.get(i);
            if (!actual.hoja) {
                for (int j = 0; j <= actual.n; j++) {
                    actual.hijos[j].nivel = actual.nivel + 1;
                    lista.add(actual.hijos[j]);
                }
            }
        }
        return lista;
    }

    private int columna;

    // calcula la columna de cada nodo: hojas de izquierda a derecha
    // y cada padre centrado encima de sus hijos
    private void calcularPosiciones(Nodo nodo) {
        int largo = texto(nodo).length();
        if (nodo.hoja) {
            nodo.inicio = columna;
            columna += largo + 2;
            nodo.centro = nodo.inicio + largo / 2;
        } else {
            for (int j = 0; j <= nodo.n; j++) {
                calcularPosiciones(nodo.hijos[j]);
            }
            nodo.centro = (nodo.hijos[0].centro + nodo.hijos[nodo.n].centro) / 2;
            nodo.inicio = nodo.centro - largo / 2;
        }
    }

    public void dibujar() {
        if (raiz == null) {
            System.out.println("No hay estudiantes registrados.\n");
            return;
        }
        List<Nodo> nodos = nodosPorNivel();
        columna = 0;
        calcularPosiciones(raiz);

        // ajusta las columnas para que nada quede encimado
        int minimo = 0;
        for (Nodo nodo : nodos) {
            minimo = Math.min(minimo, nodo.inicio);
        }
        int ancho = 0;
        int fin = -2;
        for (int v = 0; v < nodos.size(); v++) {
            Nodo nodo = nodos.get(v);
            if (v > 0 && nodo.nivel != nodos.get(v - 1).nivel) {
                fin = -2;
            }
            nodo.inicio -= minimo;
            nodo.centro -= minimo;
            int largo = texto(nodo).length();
            if (nodo.inicio < fin + 2) {
                nodo.inicio = fin + 2;
                nodo.centro = nodo.inicio + largo / 2;
            }
            fin = nodo.inicio + largo;
            ancho = Math.max(ancho, fin);
        }

        // imprime nivel por nivel: una linea de nodos y una de ramas
        int ultimo = nodos.get(nodos.size() - 1).nivel;
        System.out.println();
        for (int d = 0; d <= ultimo; d++) {
            char[] fila = espacios(ancho);
            char[] ramas = espacios(ancho);

            for (Nodo nodo : nodos) {
                if (nodo.nivel != d) {
                    continue;
                }
                String txt = texto(nodo);
                for (int k = 0; k < txt.length(); k++) {
                    fila[nodo.inicio + k] = txt.charAt(k);
                }
                if (!nodo.hoja) {
                    for (int j = 0; j <= nodo.n; j++) {
                        int cp = nodo.centro;
                        int ch = nodo.hijos[j].centro;
                        int pos = (cp + ch) / 2;
                        if (ch < cp - 2) {
                            ramas[pos] = '/';    // hijo a la izquierda
                        } else if (ch > cp + 2) {
                            ramas[pos] = '\\';   // hijo a la derecha
                        } else {
                            ramas[pos] = '|';    // hijo abajo
                        }
                    }
                }
            }

            String nombreNivel;
            if (d == 0 && ultimo == 0) {
                nombreNivel = "raiz (tambien es hoja)";
            } else if (d == 0) {
                nombreNivel = "raiz";
            } else if (d == ultimo) {
                nombreNivel = "hojas";
            } else {
                nombreNivel = "nodos internos";
            }
            System.out.println(new String(fila) + "     <- Nivel " + d + ": " + nombreNivel);
            if (d < ultimo) {
                System.out.println(new String(ramas));
            }
        }
        System.out.println();
    }

    // crea una linea de espacios
    private char[] espacios(int ancho) {
        char[] c = new char[ancho];
        for (int k = 0; k < ancho; k++) {
            c[k] = ' ';
        }
        return c;
    }

    // EJEMPLO: registra 8 estudiantes para probar rapido
    public void cargarEjemplo() {
        registrar(new Estudiante(1005, "Ana Gomez", "Sistemas"), false);
        registrar(new Estudiante(1012, "Carlos Perez", "Electronica"), false);
        registrar(new Estudiante(1003, "Laura Diaz", "Sistemas"), false);
        registrar(new Estudiante(1020, "Miguel Torres", "Industrial"), false);
        registrar(new Estudiante(1008, "Sofia Ruiz", "Sistemas"), false);
        registrar(new Estudiante(1015, "Andres Castro", "Civil"), false);
        registrar(new Estudiante(1001, "Valentina Rios", "Sistemas"), false);
        registrar(new Estudiante(1018, "Diego Herrera", "Electronica"), false);
        System.out.println("Se registraron 8 estudiantes de ejemplo.");
    }

    // LEER DATOS: piden un numero o un texto y lo vuelven a pedir si esta mal
    private static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!sc.hasNextLine()) {
                System.exit(0);
            }
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero valido.");
            }
        }
    }

    private static String leerTexto(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!sc.hasNextLine()) {
                System.exit(0);
            }
            String linea = sc.nextLine().trim();
            if (!linea.isEmpty()) {
                return linea;
            }
            System.out.println("Este dato no puede quedar vacio.");
        }
    }

    // MENU PRINCIPAL: se repite hasta elegir Salir
    public static void main(String[] args) {
        ArbolB registro = new ArbolB(2);   // t = 2 -> maximo 3 estudiantes por nodo
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("===== REGISTRO DE ESTUDIANTES (ARBOL B) =====");
            System.out.println("1. Registrar estudiante");
            System.out.println("2. Buscar estudiante por codigo");
            System.out.println("3. Listar estudiantes ordenados");
            System.out.println("4. Ver arbol de codigos");
            System.out.println("5. Cargar estudiantes de ejemplo");
            System.out.println("6. Salir");
            opcion = leerEntero(sc, "Opcion: ");

            switch (opcion) {
                case 1:
                    int codigo = leerEntero(sc, "Codigo: ");
                    if (registro.buscar(codigo) != null) {
                        System.out.println("Ya existe un estudiante con el codigo " + codigo + ".\n");
                        break;
                    }
                    String nombre = leerTexto(sc, "Nombre: ");
                    String carrera = leerTexto(sc, "Carrera: ");
                    registro.registrar(new Estudiante(codigo, nombre, carrera), true);
                    registro.dibujar();
                    break;
                case 2:
                    int buscado = leerEntero(sc, "Codigo a buscar: ");
                    Estudiante e = registro.buscar(buscado);
                    if (e != null) {
                        System.out.println("Encontrado: " + e);
                    } else {
                        System.out.println("No existe un estudiante con el codigo " + buscado + ".");
                    }
                    System.out.println("Nodos revisados: " + registro.ultimosPasos
                            + " de " + registro.total + " estudiantes registrados.\n");
                    break;
                case 3:
                    registro.listar();
                    break;
                case 4:
                    registro.dibujar();
                    break;
                case 5:
                    registro.cargarEjemplo();
                    registro.dibujar();
                    break;
                case 6:
                    System.out.println("Fin del programa.");
                    break;
                default:
                    System.out.println("Opcion no valida.\n");
            }
        } while (opcion != 6);
    }
}
