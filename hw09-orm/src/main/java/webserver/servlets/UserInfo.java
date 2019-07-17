package webserver.servlets;

import dbservice.DbServiceUser;
import model.User;
import webserver.models.UserJson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfo extends UserServlet {

    public UserInfo(DbServiceUser hibernateService) {
        super (hibernateService);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            System.out.println("Enter doGET");
            List<User> users = getHibernateService().getAll();
            List<UserJson> userJsons = users.stream().map(it ->
                new UserJson(it.getName(),
                        it.getAge(),
                        it.getAddress().getStreet(),
                        it.getPhoneNumbers().iterator().next().getNumber()))
                    .collect(Collectors.toList());
            String resultAsString = getGson().toJson(userJsons);

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
