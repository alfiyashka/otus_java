package ioc.repository;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    void create(User user) throws SQLException;
    List<User> getAll() throws SQLException;
}

