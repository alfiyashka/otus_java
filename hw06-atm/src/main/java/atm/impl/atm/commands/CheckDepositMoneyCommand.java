package atm.impl.atm.commands;

import atm.ICash;
import atm.impl.Banknote;
import atm.impl.atms.AbstractAtm;
import atm.impl.cell.Cell;

public class CheckDepositMoneyCommand extends AtmCommand  {

    private final ICash cash;
    private final AbstractAtm atm;

    public CheckDepositMoneyCommand(ICash cash, AbstractAtm atm) {
        this.cash = cash;
        this.atm = atm;
    }

    public ICash perform () {
        int atmBalance = atm.balance();
        int totalMoney = cash.balance();
        if (totalMoney + atmBalance > atm.maxAvailableBalance()) {
            throw new RuntimeException("Cannot deposit money: atm is full.");
        }
        for (final Banknote banknote : cash.getBanknotes()) {
            Cell cell = atm.cells().stream().filter(it -> it.nominal().equals(banknote.nominal()))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Cannot deposit money: unsupported banknote value "
                            + banknote.nominal().getValue()));
            if (banknote.amount() + cell.amount() > Cell.MAX_BANKNOTES_AMOUNT) {
                throw new RuntimeException("Cannot deposit money: cell of banknote value "
                        + banknote.nominal().getValue() + " is full");
            }
        }
        return checkNext();
    }
}
