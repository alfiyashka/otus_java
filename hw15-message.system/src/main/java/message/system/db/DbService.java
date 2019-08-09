package message.system.db;

import message.system.messageSystem.Addressee;
import model.User;

import java.sql.SQLException;
import java.util.List;

public interface DbService extends Addressee {
    List<User> getAll() throws SQLException;

    void create(User user) throws SQLException;
}
