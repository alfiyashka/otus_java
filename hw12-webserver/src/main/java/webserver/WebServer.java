package webserver;

import com.google.gson.Gson;
import dbservice.DbServiceUser;
import hibernate.DbServiceUserHibernate;
import model.Address;
import model.Phone;
import model.User;
import org.eclipse.jetty.security.*;
import org.eclipse.jetty.security.authentication.DigestAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;
import org.hibernate.cfg.Configuration;
import webserver.servlets.CreateUserServlet;
import webserver.servlets.UserInfoServlet;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class WebServer {
    private final static int PORT = 8080;

    public static void main(String[] args) throws Exception {
        new WebServer().start();
    }

    private void start() throws Exception {
        Server server = createServer(PORT);
        server.start();
        server.join();
    }

    private DbServiceUser createHibernateService() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        Class<?>[] classes = new Class<?>[] { User.class, Phone.class, Address.class };
        return new DbServiceUserHibernate(configuration, classes);
    }

    private Server createServer(int port) throws MalformedURLException {

        DbServiceUser hibernateService = createHibernateService();
        Gson gson = new Gson();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new UserInfoServlet(hibernateService, gson)), "/users");
        context.addServlet(new ServletHolder(new CreateUserServlet(hibernateService, gson)), "/userCreate");

        Server server = new Server(port);
        server.setHandler(new HandlerList(context));

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{createResourceHandler(), createSecurityHandler(context)});//
        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        URL fileDir = WebServer.class.getClassLoader().getResource("static");
        if (fileDir == null) {
            throw new RuntimeException("File Directory not found");
        }
        resourceHandler.setResourceBase(fileDir.getPath());
        return resourceHandler;
    }


    private SecurityHandler createSecurityHandler(ServletContextHandler context) {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"user", "admin"});

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/*");
        mapping.setConstraint(constraint);

        HashLoginService hashLoginService = new HashLoginService("test");
        UserStore userStore = new UserStore();
        userStore.addUser("user", new Password("password"), constraint.getRoles());
        hashLoginService.setUserStore(userStore);


        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        security.setAuthenticator(new DigestAuthenticator());

        security.setLoginService(hashLoginService);
        security.setHandler(new HandlerList(context));
        security.setConstraintMappings(Collections.singletonList(mapping));

        return security;
    }

}
