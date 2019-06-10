package atm.impl.cell;

import atm.ICash;
import atm.ICell;
import atm.ICopyable;
import atm.Nominal;
import atm.impl.Banknote;
import atm.impl.Cash;

public class CashInCell implements ICell, ICopyable {
    private final ICash cash;
    public final static int MAX_BANKNOTES_AMOUNT = 2000;

    public CashInCell() {
        cash = new Cash();
    }

    public CashInCell(CashInCell cell) {
        cash = ((Cash)cell.cash).copy();
    }

    @Override
    public int add(Banknote banknote) {
        if (cash.banknotes() > MAX_BANKNOTES_AMOUNT) {
            throw new RuntimeException("Cannot deposit money: atm is full.");
        }
        cash.add(banknote);
        return cash.balance();
    }

    @Override
    public int balance() {
        return cash.balance();
    }

    @Override
    public int issue(int amount) {
        throw new RuntimeException("Cannot issue money from CashIn cell");
    }

    @Override
    public Nominal nominal() {
        throw new RuntimeException("Cannot get nominal value from CashIn cell");
    }

    @Override
    public void fill() {
        throw new RuntimeException("Cannot fill CashIn cell");
    }

    @Override
    public int amount() {
        return cash.banknotes();
    }

    @Override
    public CashInCell copy() {
        return new CashInCell(this);
    }
}

