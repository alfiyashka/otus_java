package ioc.controller;

import dbservice.DbServiceUser;
import ioc.dto.UserDto;
import ioc.dto.UserDtoConvertor;
import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController
{
    private final DbServiceUser dbServiceUser;
    public UserController(DbServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @GetMapping({"/", "/user/list"})
    public String userList(Model model) throws SQLException {
        List<User> users = dbServiceUser.getAll();
        List<UserDto> dtoUsers =
                users != null
                        ? users
                        .stream()
                        .map(UserDtoConvertor::convertToUserDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>();
        model.addAttribute("users", dtoUsers);
        return "usersList.html";
    }

    @GetMapping("/user/create")
    public String userCreate(Model model) {
        model.addAttribute("user", new UserDto());
        return "createUser.html";
    }

    @PostMapping("/user/save")
    public RedirectView userSave(@ModelAttribute UserDto userDto) throws SQLException {
        User user = UserDtoConvertor.convertToUser(userDto);
        dbServiceUser.create(user);
        return new RedirectView("/user/list", true);
    }
}
