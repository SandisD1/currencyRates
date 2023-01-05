package app.util;

import org.mariadb.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {

        Driver mariaDBDriver = new Driver();
        DriverManager.registerDriver(mariaDBDriver);

        String username = "example-user";
        String password = "my_cool_secret";
        String connectionString = "jdbc:mariadb://host.docker.internal/currencydb";

        return DriverManager.getConnection(connectionString, username, password);
    }


}
