package hibernate;

import cache.CacheEngine;
import dbservice.DbServiceUser;
import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;

public class DbServiceUserHibernate implements DbServiceUser {

    private CacheEngine<Long, User> cacheEngine;

    public DbServiceUserHibernate(Configuration configuration,
                                  Class<?>[] annotatedClasses) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        MetadataSources metadataSources = new MetadataSources(registry);
        for (Class<?> clazz: annotatedClasses) {
            metadataSources.addAnnotatedClass(clazz);
        }

        sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
    }

    public DbServiceUserHibernate setCacheEngine(CacheEngine<Long, User> cacheEngine) {
        this.cacheEngine = cacheEngine;
        return this;
    }

    private boolean isEnableCache() {
        return cacheEngine != null;
    }

    public int getCacheMissCount() {
        return (isEnableCache()) ? cacheEngine.getMissCount() : -1;
    }

    public int getCacheHitCount() {
        return (isEnableCache()) ? cacheEngine.getHitCount() : -1;
    }

    private SessionFactory sessionFactory;

    private User getFromSession(Long id, HibernateTransactionFunction<Long, User> getFunction) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = getFunction.apply(session, id);
            session.getTransaction().commit();
            return user;
        }
        catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }

    private void performInSession(User user, HibernateTransactionConsumer<User> hibernateFunction) {
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            hibernateFunction.perform(session, user);
            session.getTransaction().commit();
            if (isEnableCache()) {
                cacheEngine.put(user.getId(), user);
            }
        }
        catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void create(User user) {
        try {
            performInSession(user, (session, user1) -> session.save(user1));
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
        try {
            performInSession(user, (session, user1) -> session.update(user1));
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
        try {
            performInSession(user, (session, user1) -> session.saveOrUpdate(user1));
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
        try {
            User user = cacheEngine.get(id).orElseGet(
                    () -> getFromSession(id, (session, id1) -> session.load(User.class, id1))
            );
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
