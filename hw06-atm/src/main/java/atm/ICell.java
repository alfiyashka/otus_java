package atm;

public interface ICell {
    int add(int amount);
    int balance();
    int issue(int amount);
    int nominalValue();
    void fill();
    int amount();
}
