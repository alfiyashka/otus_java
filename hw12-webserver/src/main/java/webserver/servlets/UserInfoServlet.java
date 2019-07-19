package webserver.servlets;

import com.google.gson.Gson;
import dbservice.DbServiceUser;
import model.User;
import webserver.dto.UserDto;
import webserver.dto.UserDtoConvertor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoServlet extends UserServlet {

    public UserInfoServlet(DbServiceUser hibernateService, Gson gson) {
        super (hibernateService, gson);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            System.out.println("Enter doGET");
            List<User> users = getHibernateService().getAll();
            List<UserDto> userDtos = users.stream().map(
                    UserDtoConvertor::convertToUserDto)
                    .collect(Collectors.toList());
            String resultAsString = getGson().toJson(userDtos);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter printWriter = response.getWriter();
            printWriter.print(resultAsString);
            printWriter.flush();
        }
        catch (Exception e){
            setResponseData(response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Occurred following exception: " + e.getMessage());
        }
    }
}

