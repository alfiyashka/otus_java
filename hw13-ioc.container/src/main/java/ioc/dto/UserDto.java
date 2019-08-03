package ioc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private int age;
    private String address;
    private String phone;

    public UserDto(User user){
        name = user.getName();
        age = user.getAge();
        address = user.getAddress().getStreet();
        phone = user.getPhoneNumbers().iterator().next().getNumber();
    }
}
