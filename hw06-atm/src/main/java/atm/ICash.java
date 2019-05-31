package atm;

import atm.impl.Banknote;

import java.util.Set;

public interface ICash {
    void add(Banknote banknote);

    int balance();

    Set<Banknote> getBanknotes();

    void dump();

    int banknotes();
}
