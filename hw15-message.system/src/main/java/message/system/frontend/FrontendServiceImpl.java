package message.system.frontend;

import message.system.context.MessageSystemContext;
import message.system.dto.UserDto;
import message.system.frontend.domain.Msg;
import message.system.messageSystem.Address;
import message.system.messageSystem.Message;
import message.system.messageSystem.MessageSystem;
import message.system.messages.MsgSaveUser;
import message.system.messages.MsgUsersList;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontendServiceImpl implements FrontendService {
    private final Address address;
    private final MessageSystemContext context;
    private final SimpMessageSendingOperations sendingOperations;

    public FrontendServiceImpl(MessageSystemContext context, SimpMessageSendingOperations sendingOperations) {
        this.context = context;
        this.address = context.getFrontAddress();
        this.sendingOperations = sendingOperations;
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public void saveUser(UserDto user) {
        Message message = new MsgSaveUser(getAddress(), context.getDbAddress(), user);
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public void getUsers() {
        Message message = new MsgUsersList(getAddress(), context.getDbAddress());
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }

    @Override
    public void sendUsers(List<UserDto> users) {
        sendingOperations.convertAndSend("/topic/response", new Msg("send", null, users));
    }
}
