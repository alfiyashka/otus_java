package message.system;

import dbservice.DbServiceUser;
import hibernate.DbServiceUserHibernate;
import model.Address;
import model.Phone;
import model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    @Bean
    public DbServiceUser dbServiceUser() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure("hibernate.cfg.xml");
        Class<?>[] classes = new Class<?>[] { User.class, Phone.class, Address.class };
        DbServiceUser hibernateService = new DbServiceUserHibernate(configuration, classes);
        return hibernateService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
