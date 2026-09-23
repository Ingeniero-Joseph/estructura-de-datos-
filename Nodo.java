/**
 * clase que modela el nodo del árbol binario
 * @ Project: ArbolBinario
 * @ File Name: Nodo.java
 * @author: Eduardo Baquero - Miguel Hernández
 * @version 1.0
 */
public class Nodo {

    private int info;
    private Nodo izqNodo;
    private Nodo derNodo;


    /**
     * Constructor Nodo
     * Inicializan los atributos de la clase con los valores por defecto
     * @param dato
     */

    public Nodo() {
        this.info= 0;
        this.izqNodo=null;
        this.derNodo=null;
    }

    /**
     * Constructor Nodo
     * Inicializa el atributo info de la clase.
     * @param info
     */
    public Nodo(int x){
        this.info= x;
        this.izqNodo=null;
        this.derNodo=null;
    }

    /**
     * Se obtiene el info del nodo
     * @return  valor entero correspondiente al dato
     */

    public int getInfo() {
        return info;
    }

    /**
     * Se actualiza el dato del nodo
     * @param info
     */
    public void setInfo(int info) {
        this.info = info;
    }

    /**
     * Obtiene la referencia IzqNodo
     * @return  referencia del anterior nodo
     */

    public Nodo getIzqNodo() {
        return izqNodo;
    }

    /**
     * Se actualiza la referencia izqNodo del nodo
     * @param izqNodo
     */

    public void setIzqNodo(Nodo izqNodo) {
        this.izqNodo = izqNodo;
    }

    /**
     * Obtiene la referencia derNodo
     * @return  referencia del anterior nodo
     */

    public Nodo getDerNodo() {
        return derNodo;
    }

    /**
     * Se actualiza la referencia derNodo del nodo
     * @param derNodo
     */

    public void setDerNodo(Nodo derNodo) {
        this.derNodo = derNodo;
    }
}
