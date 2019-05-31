package atm.impl.cell;

import atm.ICell;
import atm.ICopyable;
import atm.Nominal;
import atm.impl.Banknote;

public class Cell implements ICell, Comparable<Cell>, ICopyable {

    public final static int MAX_BANKNOTES_AMOUNT = 2000;
    private final Nominal nominal;
    private int amount;

    public Cell(Nominal nominal, int amount) {
        this.nominal = nominal;
        this.amount = amount;
    }

    // full cells
    public Cell(Nominal nominal) {
        this.nominal = nominal;
        this.amount = MAX_BANKNOTES_AMOUNT;
    }

    public Cell(Cell cell) {
        this.nominal = cell.nominal;
        this.amount = cell.amount;
    }

    public Nominal nominal() {
        return nominal;
    }

    @Override
    public void fill() {
        amount = MAX_BANKNOTES_AMOUNT;
    }


    @Override
    public int add(Banknote banknote) {
        if (!banknote.nominal().equals(nominal)) {
            throw new RuntimeException("Cannot issue money with nominal '" + banknote.nominal()
                    + "' to cell with nominal '" + nominal + "'");
        }

        int remainder = MAX_BANKNOTES_AMOUNT - (this.amount + banknote.amount());
        if (remainder < 0) {
            this.amount = MAX_BANKNOTES_AMOUNT;
            return this.amount * nominal.getValue();
        }
        this.amount += banknote.amount();
        return banknote.amount() * nominal.getValue();
    }

    @Override
    public int balance() {
        return nominal.getValue() * amount;
    }

    @Override
    public int issue(int amount) {
        if (this.amount < amount) {
            this.amount = 0;
            return this.amount * nominal.getValue();
        }
        this.amount -= amount;
        return amount * nominal.getValue();
    }

    @Override
    public int compareTo(Cell o) {
        if (this.nominal.getValue() > o.nominal.getValue()) {
            return 1;
        }
        else if (this.nominal.getValue() < o.nominal.getValue()) {
            return -1;
        }
        else {
            return 0;
        }
    }

    @Override
    public int amount() {
        return amount;
    }

    @Override
    public ICopyable copy() {
        return new Cell(this);
    }
}
