package orm;

import java.sql.Connection;

public interface JdbcH2ConnectorHelper {
    Connection connection();
    void connect();
    void disconnect();
}
