package ioc;

import cache.CacheEngine;
import cache.CacheEngineImpl;
import dbservice.DbServiceUser;
import hibernate.DbServiceUserHibernate;
import model.Address;
import model.Phone;
import model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@ComponentScan
@EnableWebMvc
public class WebConfig {

    private final ApplicationContext applicationContext;

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Bean
    public CacheEngine cacheEngine() {
        CacheEngine<Long, User> cacheEngine = new CacheEngineImpl<>
                (15, 1000, 0, true);
        return cacheEngine;
    }

    @Bean
    public DbServiceUser dbServiceUser() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure("hibernate.cfg.xml");
        Class<?>[] classes = new Class<?>[] { User.class, Phone.class, Address.class };
        DbServiceUser hibernateService = new DbServiceUserHibernate(configuration, classes); //, cacheEngine());
        return hibernateService;
    }

}
