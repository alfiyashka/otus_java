package message.system.context;

import message.system.messageSystem.Address;
import message.system.messageSystem.MessageSystem;
import org.springframework.stereotype.Service;

@Service
public class MessageSystemContext {
    private final MessageSystem messageSystem;

    MessageContextAddress address;

    public MessageSystemContext(MessageSystem messageSystem, MessageContextAddress address) {
        this.messageSystem = messageSystem;
        this.address = address;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public Address getFrontAddress() {
        return address.getFrontAddress();
    }

    public Address getDbAddress() {
        return address.getFrontAddress();
    }
}

