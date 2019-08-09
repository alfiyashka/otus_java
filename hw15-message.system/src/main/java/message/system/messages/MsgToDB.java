package message.system.messages;

import message.system.db.DbService;
import message.system.messageSystem.Address;
import message.system.messageSystem.Addressee;
import message.system.messageSystem.Message;

import java.sql.SQLException;

public abstract class MsgToDB extends Message {
    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        try {
            if (addressee instanceof DbService) {
                exec((DbService) addressee);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Cannot execute because of following error: " + e.getMessage(), e);
        }
    }

    public abstract void exec(DbService dbService) throws SQLException;
}
