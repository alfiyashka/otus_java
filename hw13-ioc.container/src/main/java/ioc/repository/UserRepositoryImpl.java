package ioc.repository;

import dbservice.DbServiceUser;
import model.User;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private DbServiceUser dbServiceUser;

    public UserRepositoryImpl(DbServiceUser dbService) {
        this.dbServiceUser = dbService;
    }

    @Override
    public void create(User user) throws SQLException {
        dbServiceUser.create(user);
    }

    @Override
    public List<User> getAll() throws SQLException {
        return dbServiceUser.getAll();
    }
}
