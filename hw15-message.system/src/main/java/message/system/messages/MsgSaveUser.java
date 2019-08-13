package message.system.messages;


import message.system.db.DbService;
import message.system.dto.UserDto;
import message.system.dto.UserDtoConvertor;
import message.system.messageSystem.Address;
import model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MsgSaveUser extends MsgToDB {

    private final UserDto userDto;

    public MsgSaveUser(Address from, Address to, UserDto userDto) {
        super(from, to);
        this.userDto = userDto;
    }

    @Override
    public void exec(DbService dbService) throws SQLException {
        User user = UserDtoConvertor.convertToUser(userDto);
        dbService.create(user);
        List<User> users = dbService.getAll();
        List<UserDto> dtoUsers =
                users != null
                        ? users
                        .stream()
                        .map(UserDtoConvertor::convertToUserDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>();
        dbService.getMS().sendMessage(new MsgUsersListAnswer(getTo(), getFrom(), dtoUsers));
    }
}

