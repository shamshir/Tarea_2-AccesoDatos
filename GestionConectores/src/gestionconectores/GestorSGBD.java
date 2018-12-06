package gestionconectores;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import libreriaIoc.Article;

public class GestorSGBD {

    private Connection conection = null;
    private final String url = "jdbc:mysql://localhost:3306/lunarlander";
    private final String user = "root";
    private final String password = "root";

    public GestorSGBD() {

    }

    public void crearEstructura() {

        conectar();

        crearTablas();

        cerrarConexion();

    }

    public void eliminarEstructura() {

        conectar();

        borrarTablas();

        cerrarConexion();

    }

    public void ejecutarSentenciaSql(String query) {

        conectar();

        String queryType = query.substring(0, 6);

        if (queryType.toUpperCase().equals("SELECT")) {
            aplicarConsulta(query);
        } else {
            aplicarSentencia(query);
        }

        cerrarConexion();

    }

    public Article obtenerArticulo(int id) {
        
        conectar();
        
        Article art = null;
        try {
            Statement st = conection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Article WHERE id=" + id + ";");
            if (rs.next()) {
                art = new Article(rs.getInt("id"), rs.getString("descripcio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se ha podido obtener el artículo");
        }
        
        cerrarConexion();
        
        return art;
    }

    public List<Article> obtenerArticulos() {
        
        conectar();
        
        List<Article> listaArt = new ArrayList<Article>();
        try {
            Statement st = conection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Article;");
            while (rs.next()) {
                listaArt.add(new Article(rs.getInt("id"), rs.getString("descripcio")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se han podido obtener los artículos");
        }
        
        cerrarConexion();
        
        return listaArt;
    }

    public List<Article> obtenerArticulosSegunDescripcion(String descripcion) {
        
        conectar();
        
        List<Article> listaArt = new ArrayList<Article>();
        try {
            Statement st = conection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Article WHERE descripcio LIKE '%" + descripcion + "%';");
            while (rs.next()) {
                listaArt.add(new Article(rs.getInt("id"), rs.getString("descripcio")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se han podido obtener los artículos");
        }
        
        cerrarConexion();
        
        return listaArt;
    }

    private void conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conection = DriverManager.getConnection(url, user, password);
            if (this.conection != null) {
                System.out.println("Conectado a " + conection.toString());
            }
        } catch (SQLException e) {
            System.out.println("Error - url, usuario o contraseña erroneo/a");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error - clase no encontrada");
            e.printStackTrace();
        }
    }

    private void cerrarConexion() {
        try {
            this.conection.close();
            System.out.println("Conexión cerrada");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cerrar conexión");
        }
    }

    private void crearTablas() {
        try {
            String sql;
            Statement st = this.conection.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS Article ("
                    + "id INT AUTO_INCREMENT,"
                    + "descripcio VARCHAR(255),"
                    + "PRIMARY KEY (id));";
            st.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS Sector ("
                    + "id INT AUTO_INCREMENT,"
                    + "descripcio VARCHAR(255),"
                    + "PRIMARY KEY (id));";
            st.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS ArticlesPerSector ("
                    + "sectorId INT NOT NULL,"
                    + "articleId INT NOT NULL,"
                    + "PRIMARY KEY (sectorId, articleId),"
                    + "FOREIGN KEY (sectorId) REFERENCES Sector (id),"
                    + "FOREIGN KEY (articleId) REFERENCES Article (id));";
            st.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS Comercial ("
                    + "nif VARCHAR(9) NOT NULL,"
                    + "nom VARCHAR(25),"
                    + "PRIMARY KEY (nif));";
            st.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS Zona ("
                    + "id INT AUTO_INCREMENT,"
                    + "descripcio VARCHAR(255),"
                    + "comercialNif VARCHAR(9),"
                    + "PRIMARY KEY (id),"
                    + "FOREIGN KEY (comercialNif) REFERENCES Comercial (nif));";
            st.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS Client ("
                    + "id INT AUTO_INCREMENT,"
                    + "nif VARCHAR(9) NOT NULL,"
                    + "nom VARCHAR(25),"
                    + "zonaId INT,"
                    + "sectorId INT,"
                    + "PRIMARY KEY (id),"
                    + "FOREIGN KEY (zonaId) REFERENCES Zona (id),"
                    + "FOREIGN KEY (sectorId) REFERENCES Sector (id));";
            st.executeUpdate(sql);
            System.out.println("Tablas creadas");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se han podido crear todas las tablas");
        }
    }

    private void borrarTablas() {
        try {
            String sql = "DROP TABLE IF EXISTS Client, Zona, Comercial, ArticlesPerSector, Sector, Article;";
            Statement st = this.conection.createStatement();
            st.executeUpdate(sql);
            System.out.println("Tablas Borradas");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se han podido borrar las tablas");
        }
    }

    private void aplicarSentencia(String sql) {
        try {
            Statement st = conection.createStatement();
            st.executeUpdate(sql);
            System.out.println("Sentencia '" + sql + "' ejecutada");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Sentencia fallida");
        }
    }

    private ResultSet aplicarConsulta(String sql) {
        try {
            Statement st = conection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            imprimirResultado(rs);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void imprimirResultado(ResultSet rs) {
        try {
            ResultSetMetaData rsMeta = rs.getMetaData();
            int colNumber = rsMeta.getColumnCount();
            System.out.println("\n---- Resultado Consulta ----");
            while (rs.next()) {
                for (int i = 1; i <= colNumber; i++) {
                    if (i > 1) {
                        System.out.print(", ");
                    }
                    String colValue = rs.getString(i);
                    System.out.print(rsMeta.getColumnName(i) + " " + colValue);
                }
                System.out.println();
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
