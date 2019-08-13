package message.system.db;

import dbservice.DbServiceUser;
import message.system.context.MessageSystemContext;
import message.system.messageSystem.Address;
import message.system.messageSystem.MessageSystem;
import model.User;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class DbServiceImpl implements DbService {
    private final DbServiceUser dbServiceUser;
    private final Address address;
    private final MessageSystemContext context;

    public DbServiceImpl(DbServiceUser dbServiceUser, MessageSystemContext context) {
        this.dbServiceUser = dbServiceUser;
        this.context = context;
        this.address = context.getDbAddress();
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public List<User> getAll()  throws SQLException {
        return dbServiceUser.getAll();
    }

    @Override
    public void create(User user) throws SQLException {
        dbServiceUser.create(user);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }
}

