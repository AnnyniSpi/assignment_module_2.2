package dev.annyni;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * todo Document type DriverJDBC
 */
public class DriverJDBC {
    private static final String PROPERTIES_FILE = "src/main/resources/application.properties";
    private static Connection connection;

    private static Connection getConnection(){
        if (connection == null){
            try {
                Properties properties = new Properties();
                properties.load(Files.newInputStream(Paths.get(PROPERTIES_FILE)));

                String url = properties.getProperty("db.url");
                String username = properties.getProperty("db.username");
                String password = properties.getProperty("db.password");

                Class.forName(properties.getProperty("db.driver"));

                connection = DriverManager.getConnection(url, username, password);
            } catch (IOException | ClassNotFoundException | SQLException e) {
                System.out.println("Ошибка при подключение: " + e);
                System.exit(1);
            }
        }
        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    public static PreparedStatement getPreparedStatementWithKey(String sql) throws SQLException {
        return getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }
}
