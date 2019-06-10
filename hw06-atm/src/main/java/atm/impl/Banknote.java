package atm.impl;

import atm.Nominal;

public class Banknote {
    private Nominal nominal;
    private Integer amount;

    public Banknote(Nominal nominal, Integer amount) {
        this.nominal = nominal;
        this.amount = amount;
    }

    public Nominal nominal() {
        return nominal;
    }

    public Integer amount() {
        return amount;
    }
}
