package Datos;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        // 1. Cargar configuración desde db.properties
        Properties props = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("No se encontró el archivo db.properties");
                return;
            }
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // 2. Obtener datos de conexión
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        // 3. Probar conexión
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexión establecida con éxito a la base de datos.");

            // Mostrar metadatos
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("🔹 Driver: " + meta.getDriverName());
            System.out.println("🔹 Versión del driver: " + meta.getDriverVersion());
            System.out.println("🔹 Base de datos: " + meta.getDatabaseProductName());
            System.out.println("🔹 Versión BD: " + meta.getDatabaseProductVersion());
            System.out.println("🔹 Usuario conectado: " + meta.getUserName());
            System.out.println("🔹 URL de conexión: " + meta.getURL());

            String select = "SELECT * FROM empleados";
            // 4. Listar empleados con Statement
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(select);
            System.out.println("\n=== EMPLEADOS ===");
            while (rs.next()) {
                System.out.printf("ID: %d | Nombre: %s | Salario: %.2f €%n",
                        rs.getInt("id"), rs.getString("nombre"), rs.getDouble("salario"));
            }

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnas = rsmd.getColumnCount();

            System.out.println("\n=== Información del ResultSetMetaData ===");
            System.out.println("Número de columnas: " + columnas);

            for (int i = 1; i <= columnas; i++) {
                System.out.println("Columna " + i + ": " + rsmd.getColumnName(i) +
                        " | Tipo: " + rsmd.getColumnTypeName(i));
            }

            DatabaseMetaData dbmd = con.getMetaData();
            System.out.println("\n=== Información del DatabaseMetaData ===");
            System.out.println("Nombre del producto de la base de datos: " + dbmd.getDatabaseProductName());
            System.out.println("Versión del producto de la base de datos: " + dbmd.getDatabaseProductName());
            System.out.println("Nombre del driver JDBC: " + dbmd.getDriverName());



        }catch (SQLException e){
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());

        }
        }
}
