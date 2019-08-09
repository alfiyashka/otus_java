package message.system.frontend.domain;

import lombok.*;
import message.system.dto.UserDto;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Msg {
    private String method;
    private UserDto user;
    private List<UserDto> users;
}