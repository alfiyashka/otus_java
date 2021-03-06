package webserver.servlets;

import com.google.gson.Gson;
import dbservice.DbServiceUser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserServlet  extends HttpServlet {
    private final Gson gson;
    private final DbServiceUser hibernateService;

    public UserServlet(DbServiceUser hibernateService, Gson gson) {
        this.hibernateService = hibernateService;
        this.gson = gson;
    }

    protected DbServiceUser getHibernateService() {
        return hibernateService;
    }

    protected  Gson getGson() {
        return gson;
    }

    protected void setResponseData(HttpServletResponse response,
                                   int responseCode,
                                   String data) throws IOException {
        response.setContentType("application/json");
        response.setStatus(responseCode);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(data);
        printWriter.flush();
    }
}

