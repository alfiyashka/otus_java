package atm.impl;

import atm.ICell;
import atm.Nominal;

public class Cell implements ICell, Comparable<Cell>{

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

    public int nominalValue() {
        return nominal.getValue();
    }

    @Override
    public void fill() {
        amount = MAX_BANKNOTES_AMOUNT;
    }

    @Override
    public int add(int amount) {
        int remainder = MAX_BANKNOTES_AMOUNT - (this.amount + amount);
        if (remainder < 0) {
            this.amount = MAX_BANKNOTES_AMOUNT;
            return this.amount * nominalValue();
        }
        this.amount += amount;
        return amount *  nominalValue();
    }

    @Override
    public int balance() {
        return nominal.getValue() * amount;
    }

    @Override
    public int issue(int amount) {
        if (this.amount < amount) {
            this.amount = 0;
            return this.amount * nominalValue();
        }
        this.amount -= amount;
        return amount * nominalValue();
    }

    @Override
    public int compareTo(Cell o) {
        if (this.nominalValue() > o.nominalValue()) {
            return 1;
        }
        else if (this.nominalValue() < o.nominalValue()) {
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

}