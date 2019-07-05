package hibernate;

import dbservice.DbServiceUser;
import model.Address;
import model.Phone;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;

public class DbServiceUserHibernate implements DbServiceUser {

    public DbServiceUserHibernate() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        Metadata metadata = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Phone.class)
                .buildMetadata();

        sessionFactory = metadata.buildSessionFactory();
    }

    private SessionFactory sessionFactory;

    @Override
    public void create(User user) {
        try (var session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            System.out.println("-->Created user: " + user);
        }
        catch (Exception e) {
            throw new RuntimeException(
                    String.format("Cannot save user with id %d because of error: %s",
                            user.getId(), e.getMessage()),
                    e);
        }
    }

    @Override
    public void update(User user)  {
        try (var session = sessionFactory.openSession()){
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            System.out.println("-->Updated user: " + user);
        }
        catch (Exception e) {
            throw new RuntimeException(
                    String.format("Cannot update user with id %d because of error: %s",
                            user.getId(), e.getMessage()),
                    e);
        }
    }

    @Override
    public void createOrUpdate(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();
            System.out.println("-->Create or updated user: " + user);
        }
        catch (Exception e) {
            throw new RuntimeException(
                    String.format("Cannot createOrUpdate user with id %d because of error: %s",
                            user.getId(), e.getMessage()),
                    e);
        }
    }

    @Override
    public User load(long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            System.out.println("-->Loaded user: " + user);
            return user;
        }
        catch (Exception e) {
            throw new RuntimeException(
                    String.format("Cannot load user with id %d because of error: %s", id, e.getMessage()),
                    e);
        }
    }

    @Override
    public void close() throws SQLException {
        sessionFactory.close();
    }
}
