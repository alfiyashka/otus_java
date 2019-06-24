package orm.impl;

import orm.JdbcH2ConnectorHelper;
import orm.TableCreator;

import java.sql.Connection;

public class H2TableCreatorImpl implements TableCreator {
    private final Connection connection;

    public H2TableCreatorImpl() {
        JdbcH2ConnectorHelper h2ConnectorHelper = new JdbcH2ConnectorHelperImpl(
                "jdbc:h2:tcp://localhost:9092/~/test_db",
                "sa",
                "");
        h2ConnectorHelper.connect();
        connection = h2ConnectorHelper.connection();
    }

    private static final String createUserTblStatement = "CREATE TABLE IF NOT EXISTS User (id bigint(20) NOT NULL auto_increment, " +
            " name varchar(255), age int(3))";

    private static final String createAccountTblStatement = "CREATE TABLE IF NOT EXISTS Account (no bigint(20) NOT NULL auto_increment, " +
            " type varchar(255), rest number) ";

    private void createTableIfNotExist(String statement) {
        try {
            connection.prepareStatement(statement).execute();
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot create table because of error: " + e.getMessage());
        }
    }

    @Override
    public void createUserTableIfNotExist() {
        createTableIfNotExist(createUserTblStatement);
    }

    @Override
    public void createAccountTableIfNotExist() {
        createTableIfNotExist(createAccountTblStatement);
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot close connection because of error: " + e.getMessage());
        }
    }
}

