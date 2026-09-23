import java.util.Scanner;

/**
 * clase que modela la interfaz de usuario
 * @ Project: ArbolBinario
 * @ File Name: AppArbol.java
 * @author: Eduardo Baquero - Miguel Hernández
 * @version 1.0
 */

public class AppArbol {

    private Arbol arbol;


    private int dato;
    private Scanner sc;
    private Nodo nodo;

    /**
     * creación del control del árbol
     */
    public AppArbol()
    {
        this.sc = new Scanner(System.in);
        this.dato=0;
        this.arbol=new Arbol(dato);
        this.nodo= new Nodo();


    }

    /**
     * Método menuOpcion
     * opciones correspondientes a las operaciones en una lista.
     */

    public void menuOpcion()
    {
        int numero;
        nodo=arbol.obtenerRaiz();
        do{
            System.out.println(imprimirMenu());
            numero=sc.nextInt ();
            switch(numero)
            {
                case 1:
                    crearArbol();
                    break;
                case 2:
                    arbol.imprimirInOrden(nodo);
                    System.out.println("");
                    break;
                case 3:
                    arbol.imprimirPreOrden(nodo);
                    System.out.println("");
                    break;
                case 4:
                    arbol.imprimirPostOrden(nodo);
                    System.out.println("");
                    break;
                case 5:
                    buscarDato();
                    break;
                default:
                    break;
            }

        }while(numero!=6);
    }

    private String imprimirMenu( ) {
        String opcion = "";
        opcion = "Operaciones en Arboles Binarios\n\n"+
                "1. Crear Arbol\n"+
                "2. Imprimir InOrder\n"+
                "3. Imprimir Preorden\n"+
                "4. Imprimir PostOrden\n"+
                "5. Buscar Elemento\n"+
                "6. Salir\n"+
                "\nSeleccione opcion ";

        return opcion;

    }

    /**
     * Método que itera mientras que los datos sean diferentes de cero
     * para la creación del árbol
     * para invocar el método de anexar adicionar nodos de la clase Arbol
     */

    public void crearArbol()
    {
        dato=1;
        while(dato!=0)
        {
            System.out.println("Digite Numero");
            dato = sc.nextInt ();
            if(dato!=0)
                arbol.anexarNodo(dato);
        }
    }

    /**
     * Método lee el numero a buscar en el nodo
     */

    public void buscarDato()
    {
        dato=1;
        System.out.println("Digite Numero");
        dato = sc.nextInt ();
        arbol.buscarElemlento(dato,nodo);
    }

    /**
     * Método main que es la entrada de toda aplicación en Java
     * @param args
     */
    public static void main(String[] args) {
        AppArbol opcion =new AppArbol();
        opcion.menuOpcion();

    }
}
