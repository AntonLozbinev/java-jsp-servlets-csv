package my.csvuploader.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DaoFactory {

    private static DaoFactory factory;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        if(factory == null) {
            factory = new DaoFactory();
        }
        return factory;
    }

    private Connection getConnection() throws DaoException {
        Properties prop = new Properties();
        Connection connection;
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("database.properties")){
            prop.load(in);
            String driver = prop.getProperty("jdbc.driverClassName");
            String url = prop.getProperty("jdbc.url");
            String user = prop.getProperty("jdbc.username");
            String pass = prop.getProperty("jdbc.password");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new DaoException("Can not create connection", e);
        }
        return connection;
    }

    public ContactDao getContactDao() throws DaoException {
        return new MySqlContactDao(getConnection());
    }
}
