package orm;

public interface TableCreator {
    void createUserTableIfNotExist();
    void createAccountTableIfNotExist();
    void closeConnection();
}
