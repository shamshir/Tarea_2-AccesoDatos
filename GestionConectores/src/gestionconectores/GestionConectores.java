package gestionconectores;

import java.util.List;
import libreriaIoc.Article;

public class GestionConectores {

    public static void main(String[] args) {
        
        GestorSGBD gestor = new GestorSGBD();
        
//        /* Crear Tablas */
//        gestor.crearEstructura();
        
//        /* Eliminar Tablas */
//        gestor.eliminarEstructura();

//        /* Introducir valores en tabla Articles */
//        gestor.ejecutarSentenciaSql("INSERT INTO Article VALUES "
//                + "(1, 'Galletas'), "
//                + "(2, 'Chocolate'), "
//                + "(3, 'Tomate con pollo'), "
//                + "(4, 'Tomate solo'), "
//                + "(5, 'Tomate con patatas'), "
//                + "(6, 'Salchichas'), "
//                + "(7, 'Pimientos');");
        
//        /* Modificar un artículo en tabla Articles */
//        gestor.ejecutarSentenciaSql("UPDATE Article "
//                + "SET descripcio = 'Hamburguesa' "
//                + "WHERE id = 6;");

//        /* Visualizar datos de tabla Articles */
//        gestor.ejecutarSentenciaSql("SELECT * FROM Article;");
        
//        /* Obtener un elemento de la tabla Articles en función de su ID */
//        Article art = gestor.obtenerArticulo(6);
//        imprimirArticulo(art);

//        /* Obtener una lista con todos los elementos de tabla Articles */
//        List<Article> lista = gestor.obtenerArticulos();
//        imprimirLista(lista);
        
//        /* Obtener una lista de elementos con descripción similar de la tabla Articles */
//        List<Article> lista2 = gestor.obtenerArticulosSegunDescripcion("Tomate");
//        imprimirLista(lista2);

    }
    
    public static void imprimirArticulo(Article art) {
        System.out.println("Artículo Individual");
        System.out.println("ID del Artículo: " + art.getId());
        System.out.println("Descripción del Artículo: " + art.getDescripcio());
        System.out.println();
    }
    
    public static void imprimirLista(List<Article> lista) {
        System.out.println("Lista de Artículos");
        for (Article art : lista) {
            System.out.println(art.getId() + " " + art.getDescripcio());
        }
        System.out.println();
    }
}
