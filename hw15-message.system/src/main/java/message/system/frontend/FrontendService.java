package message.system.frontend;

import message.system.dto.UserDto;
import message.system.messageSystem.Addressee;

import java.util.List;

public interface FrontendService extends Addressee {

    void saveUser(UserDto user);

    void getUsers();

    void sendUsers(List<UserDto> users);
}
