package orm;

import java.sql.SQLException;

public interface JdbcExecutor<T> {
    void create(T objectData) throws SQLException;
    void update(T objectData) throws SQLException;
    void createOrUpdate(T objectData) throws SQLException;
    <T> T load(long id, Class<T> clazz);
    void close() throws  SQLException;
}
