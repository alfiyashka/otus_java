
import model.Account;
import model.User;
import orm.JdbcExecutor;
import orm.TableCreator;
import orm.impl.H2TableCreatorImpl;
import orm.impl.JdbcExecutorImpl;

public class Main {
    public static void main(String[] args) {
        try {
            TableCreator tableCreator = new H2TableCreatorImpl();
            tableCreator.createUserTableIfNotExist();
            tableCreator.createAccountTableIfNotExist();
            tableCreator.closeConnection();

            User myUser = new User(2,"Alex", 28);
            JdbcExecutor<User> jdbcExecutorUser = new JdbcExecutorImpl<>();
            jdbcExecutorUser.create(myUser);
            myUser.setAge(30);
            jdbcExecutorUser.update(myUser);
            User findUser = jdbcExecutorUser.load(2, User.class);
            jdbcExecutorUser.createOrUpdate(findUser);
            jdbcExecutorUser.close();

            System.out.println( "Find object is " +
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
}
