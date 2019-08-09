package message.system.messages;

import message.system.frontend.FrontendService;
import message.system.messageSystem.Address;
import message.system.messageSystem.Addressee;
import message.system.messageSystem.Message;

public abstract class MsgToFrontend extends Message {
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendService) {
            exec((FrontendService) addressee);
        }
    }

    public abstract void exec(FrontendService frontendService);
}
