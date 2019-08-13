package message.system.messages;

import message.system.dto.UserDto;
import message.system.frontend.FrontendService;
import message.system.messageSystem.Address;

import java.util.List;

public class MsgUsersListAnswer extends MsgToFrontend {
    private final List<UserDto> users;

    public MsgUsersListAnswer(Address from, Address to, List<UserDto> users) {
        super(from, to);
        this.users = users;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.sendUsers(users);
    }
}
