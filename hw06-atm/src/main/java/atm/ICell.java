package atm;

import atm.impl.Banknote;

public interface ICell {
    int add(Banknote banknote);
    int balance();
    int issue(int amount);
    Nominal nominal();
    void fill();
    int amount();
}
