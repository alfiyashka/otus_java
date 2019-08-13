package message.system.frontend.controller;

import message.system.frontend.FrontendService;
import message.system.frontend.domain.Msg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    private final FrontendService frontendService;

    public MessageController(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @MessageMapping({"/connect","/message"})
    @SendTo("/topic/response")
    public void getUsers() {
        frontendService.getUsers();
    }

    @MessageMapping("/save")
    @SendTo("/topic/response")
    public void saveUser(Msg message) {
        if (message.getUser() != null) {
            frontendService.saveUser(message.getUser());
        }
    }
}
