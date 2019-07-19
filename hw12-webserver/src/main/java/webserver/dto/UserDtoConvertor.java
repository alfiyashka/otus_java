package webserver.dto;

import model.Address;
import model.Phone;
import model.User;

import java.util.HashSet;
import java.util.Set;

public class UserDtoConvertor {

    public static User convertToUser(UserDto userDto) {
        User user = new User(userDto.getName(), userDto.getAge());
        Address address = new Address(userDto.getAddress(), user);
        user.setAddress(address);
        Set<Phone> phones = new HashSet<>();
        phones.add(new Phone(userDto.getPhone(), user));
        user.setPhoneNumbers(phones);
        return user;
    }

    public static UserDto convertToUserDto(User user) {
        return new UserDto(user);
    }

}
