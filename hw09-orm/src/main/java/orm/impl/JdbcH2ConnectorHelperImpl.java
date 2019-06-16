package orm.impl;

import orm.JdbcH2ConnectorHelper;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcH2ConnectorHelperImpl implements JdbcH2ConnectorHelper {
    private Connection connection = null;

    private final String url;
    private final String login;
    private final String password;

    public JdbcH2ConnectorHelperImpl(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    @Override
    public void connect() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(url, login, password);
            connection.setAutoCommit(false);
        }
        catch (Exception e){
            throw new RuntimeException("Cannot connect to database H2 because of following error:"
                    + e.getMessage());
        }
    }

    @Override
    public Connection connection() {
        return connection;
    }

    @Override
    public void disconnect(){
        try {
            connection.close();
        }
        catch (Exception e){
            throw new RuntimeException("Cannot close connection of H2 database because of following error:"
                    + e.getMessage());
        }
    }
}
