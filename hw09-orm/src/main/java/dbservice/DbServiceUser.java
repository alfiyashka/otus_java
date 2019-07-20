package dbservice;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface DbServiceUser {
    void create(User user) throws SQLException;
    void update(User user) throws SQLException;
    void createOrUpdate(User user);
    User load(long id);
    void close() throws SQLException;
    List<User> getAll() throws SQLException;
}
