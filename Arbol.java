/**
 * clase que modela las operaciones de un árbol binario
 * @ Project: ArbolBinario
 * @ File Name: Arbol.java
 * @author: Eduardo Baquero - Miguel Hernández
 * @version 1.0
 */
public class Arbol {
    private Nodo raiz;
    private Nodo p;
    private Nodo q;



    /**
     * se crea la raíz del árbol
     */
    public Arbol(int info){
        this.raiz=new Nodo();
        this.p=new Nodo();
        this.q=new Nodo();
        this.raiz.setIzqNodo(null);
        this.raiz.setDerNodo(null);
        this.raiz.setInfo(info);
        this.p=raiz;

    }

    /**
     * Método que retorna la raíz del árbol
     * @return  raiz
     */
    public Nodo obtenerRaiz(){
        return raiz;
    }

    /**
     * Método que permite evaluar por qué lado se anexa nodo.
     * por la izquierda o derecha del árbol
     * @param n
     */

    public void anexarNodo(int n){
        if(raiz.getInfo()==0){
            raiz.setInfo(n);
        }
        else{
            p=raiz;
            q=p;
            while(q!=null&&p.getInfo()!=n){
                p=q;
                if(n<p.getInfo()){
                    q=q.getIzqNodo();
                }
                else{
                    q=q.getDerNodo();
                }
                if(p.getInfo()==n){
                    System.out.println("dato repetido");
                }
            }
            if(n<p.getInfo()){
                insertarIzquierda(p,n);
            }
            else{
                insertarDerecha(p,n);
            }
        }

    }

    /**
     * Método para anexar un nodo por la izquierda del árbol
     * @param r
     * @param n
     */
    public void insertarIzquierda(Nodo r,int n){
        Nodo nuevo = new Nodo();
        nuevo.setInfo(n);
        nuevo.setIzqNodo(null);
        nuevo.setDerNodo(null);
        r.setIzqNodo(nuevo);
    }

    /**
     * Método para anexar un nodo por la derecha del árbol
     * @param r
     * @param n
     */

    public void insertarDerecha(Nodo r,int n){
        Nodo nuevo = new Nodo();
        nuevo.setInfo(n);
        nuevo.setIzqNodo(null);
        nuevo.setDerNodo(null);
        r.setDerNodo(nuevo);


    }

    public void buscarElemlento (int numero, Nodo r){
        String str = "";
        if(r == null ) {
            System.out.println(" valor no hallado");
            return ;

        }
        if (r.getInfo() == numero){
            System.out.println(" valor encontrado");
            return ;

        }
        else{
            if( numero>r.getInfo() )
                buscarElemlento( numero, r.getDerNodo() );
            else
                buscarElemlento( numero, r.getIzqNodo() );
        }
    }
    /**
     * impresión del árbol en preorden
     * @param p
     */
    public void imprimirPreOrden(Nodo p) {
        if (p!=null){
            System.out.print (" "+p.getInfo());
            imprimirPreOrden(p.getIzqNodo());
            imprimirPreOrden(p.getDerNodo());
        }

    }
    /**
     * impresión del árbol en postorden
     * @param p
     */
    public void imprimirPostOrden(Nodo p) {
        if (p!=null){
            imprimirPostOrden(p.getIzqNodo());
            imprimirPostOrden(p.getDerNodo());
            System.out.print (" "+p.getInfo());
        }

    }

    /**
     * impresión del árbol en orden
     * @param p
     */
    public void imprimirInOrden(Nodo p) {
        if (p!=null){
            imprimirInOrden(p.getIzqNodo());
            System.out.print (" "+p.getInfo());
            imprimirInOrden(p.getDerNodo());
        }

    }
}
