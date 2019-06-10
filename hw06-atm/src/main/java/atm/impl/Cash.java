package atm.impl;

import atm.ICash;
import atm.ICashCommand;
import atm.ICopyable;
import atm.impl.cash.commands.AddCashCommand;
import atm.impl.cash.commands.DumpCashCommand;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Cash implements ICash , ICopyable {
    final Set<Banknote> banknotes = new HashSet<>();


    public Cash(){
    }

    public Cash(Cash cash) {
        banknotes.addAll(cash.banknotes);
    }

    @Override
    public void add(Banknote banknote) {
        ICashCommand addCashCommand = new AddCashCommand(banknote, banknotes);
        addCashCommand.execute();
    }

    @Override
    public int balance() {
        return banknotes.stream()
                .mapToInt( e -> e.nominal().getValue() * e.amount()).sum();
    }

    @Override
    public int banknotes() {
        return banknotes.stream()
                .mapToInt( e -> e.amount()).sum();
    }

    @Override
    public Set<Banknote> getBanknotes() {
        return banknotes;
    }

    @Override
    public void dump() {
        ICashCommand dumpCashCommand = new DumpCashCommand(banknotes);
        dumpCashCommand.execute();
    }

    @Override
    public Cash copy() {
        return new Cash(this);
    }
}
