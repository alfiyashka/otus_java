package webserver.servlets;

import com.google.gson.Gson;
import dbservice.DbServiceUser;
import model.User;
import webserver.dto.UserDto;
import webserver.dto.UserDtoConvertor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class CreateUserServlet extends UserServlet {

    public CreateUserServlet(DbServiceUser hibernateService, Gson gson) {
        super(hibernateService, gson);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Enter doPost");
        request.setCharacterEncoding("UTF-8");

        String requestBody = request.getReader()
                .lines()
                .collect(Collectors.joining());

        try {
            if (!requestBody.isEmpty()) {
                System.out.println("Tried to create user: " + requestBody);
                UserDto userDto = getGson().fromJson(requestBody, UserDto.class);
                User user = UserDtoConvertor.convertToUser(userDto);
                getHibernateService().createOrUpdate(user);
                setResponseData(response,
                        HttpServletResponse.SC_CREATED,
                        "Created following user: " + user);

            } else {
                System.out.println("Request body is empty");
                setResponseData(response,
                        HttpServletResponse.SC_NOT_FOUND,
                        "Request body is empty");
            }
        }
        catch (Exception e) {
            System.out.println("Occurred following error: " + e.getMessage());
            setResponseData(response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Occurred following exception: " + e.getMessage());
        }
    }
}
