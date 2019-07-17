package webserver.servlets;

import dbservice.DbServiceUser;
import model.Address;
import model.Phone;
import model.User;
import webserver.models.UserJson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateUser extends UserServlet {

    public CreateUser(DbServiceUser hibernateService) {
        super(hibernateService);
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
                UserJson userJson = getGson().fromJson(requestBody, UserJson.class);
                User user = new User(userJson.getName(), userJson.getAge());
                Address address = new Address(userJson.getAddress(), user);
                user.setAddress(address);
                Set<Phone> phones = new HashSet<>();
                phones.add(new Phone(userJson.getPhone(), user));
                user.setPhoneNumbers(phones);
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
