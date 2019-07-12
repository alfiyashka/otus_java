
import cache.CacheEngine;
import cache.CacheEngineImpl;
import dbservice.DbServiceUser;
import hibernate.DbServiceUserHibernate;
import model.Account;
import model.Address;
import model.Phone;
import model.User;
import org.hibernate.cfg.Configuration;
import orm.JdbcExecutor;
import orm.TableCreator;
import orm.impl.H2TableCreatorImpl;
import orm.impl.JdbcExecutorImpl;

import java.util.HashSet;
import java.util.Set;

public class Main {
    private static void testJdbcTemplate () {
        try {
            TableCreator tableCreator = new H2TableCreatorImpl();
            tableCreator.createUserTableIfNotExist();
            tableCreator.createAccountTableIfNotExist();
            tableCreator.closeConnection();

            User myUser = new User("Alex", 28);
            JdbcExecutor<User> jdbcExecutorUser = new JdbcExecutorImpl<>();
            jdbcExecutorUser.create(myUser);
            myUser.setAge(30);
            jdbcExecutorUser.update(myUser);
            User findUser = jdbcExecutorUser.load(1, User.class);
            findUser.setName("Nina");
            jdbcExecutorUser.createOrUpdate(findUser);
            jdbcExecutorUser.close();

            System.out.println("Find object is " +
                    (myUser.equals(findUser) ? "equal" : " non equal"));

            Account account = new Account(3,"Some type", 12.6F);
            JdbcExecutor<Account> jdbcExecutorAccount = new JdbcExecutorImpl<>();
            jdbcExecutorAccount.create(account);
            account.setType("new type");
            jdbcExecutorAccount.update(account);
            Account findAccount = jdbcExecutorAccount.load(3, Account.class);
            jdbcExecutorAccount.createOrUpdate(findAccount);
            jdbcExecutorAccount.close();

            System.out.println( "Find object is " +
                    (account.equals(findAccount) ? "equal" : " non equal"));

        }
        catch (Exception e) {
            System.err.println("Occurred following message: " + e.getMessage());
        }
    }

    private static void testHibernateDbService() {
        try {
            User user = new User("Alex", 28);
            Address address = new Address( "kirov", user);
            user.setAddress(address);
            Set<Phone> phones = new HashSet<>();
            phones.add(new Phone("897387645857", user));
            phones.add(new Phone("723234234234", user));
            user.setPhoneNumbers(phones);

            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            Class<?>[] classes = new Class<?>[] { User.class, Phone.class, Address.class };
            DbServiceUser hibernateService = new DbServiceUserHibernate(configuration, classes);
            hibernateService.create(user);
            user.setName("Jone");
            hibernateService.update(user);
            user.getAddress().setStreet("Lesnaya street");
            hibernateService.createOrUpdate(user);
            hibernateService.load(user.getId());
            hibernateService.close();
        }
        catch(Exception e){
            System.err.println("<--Occurred following exception: " + e.getMessage());
        }
    }

    private static void testHibernateWithCache() {
        try {
            CacheEngine<Long, User> cacheEngine = new CacheEngineImpl
                    (15, 1000, 0, true);
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            Class<?>[] classes = new Class<?>[] { User.class, Phone.class, Address.class };
            DbServiceUser hibernateService = new DbServiceUserHibernate(configuration, classes)
                    .setCacheEngine(cacheEngine);

            for (int i = 1; i < 20; i++) {
                User user = new User("User" + i, 28);
                Address address = new Address("kirov, " + i, user);
                user.setAddress(address);
                Set<Phone> phones = new HashSet<>();
                phones.add(new Phone("8 (351) 123-45-6" + i, user));
                if (i % 2 == 0) {
                    phones.add(new Phone("8 (351) 654-32-1" + i, user));
                }
                user.setPhoneNumbers(phones);
                hibernateService.create(user);
                if (i % 2 == 0) {
                    Thread.sleep(200);
                    hibernateService.load(i);
                }

            }
            for (int i = 1; i < 20; i++) {
                hibernateService.load(i);
            }

            System.out.println("Cache hits: "
                    + ((DbServiceUserHibernate)hibernateService).getCacheHitCount());
            System.out.println("Cache misses: " +
                    ((DbServiceUserHibernate)hibernateService).getCacheMissCount());
        }
        catch (Exception e) {
            System.err.println("<--Occurred following exception: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        testHibernateWithCache();
    }
}
