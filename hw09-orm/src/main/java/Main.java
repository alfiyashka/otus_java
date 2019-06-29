
import dbservice.DbServiceUser;
import hibernate.DbServiceUserHibernate;
import model.Account;
import model.Address;
import model.Phone;
import model.User;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
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

            DbServiceUser hibernateService = new DbServiceUserHibernate();
            hibernateService.create(user);
            user.setName("Jone");
            hibernateService.update(user);
            user.getAddress().setStreet("Lesnaya street");
            hibernateService.createOrUpdate(user);
            hibernateService.load(user.getId());
        }
        catch(Exception e){
            System.err.println("<--Occurred following exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        testHibernateDbService();
    }
}
