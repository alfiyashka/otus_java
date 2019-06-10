package atm.impl.atm.commands;

import atm.ICash;
import atm.impl.Banknote;
import atm.impl.atms.AbstractAtm;
import atm.impl.cell.Cell;

public class DepositMoneyCommand extends AtmCommand {

    private final ICash cash;
    private final AbstractAtm atm;

    public DepositMoneyCommand(ICash cash, AbstractAtm atm) {
        this.cash = cash;
        this.atm = atm;
    }

    @Override
    public ICash perform () {
        for (final Banknote banknote : cash.getBanknotes()) {
            Cell cell = atm.cells().stream().filter(it -> it.nominal().equals(banknote.nominal()))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Cannot deposit money: unsupported banknote value"
                            + banknote.nominal().getValue()));

            cell.add(banknote);
        }
        return cash;
    }
}
