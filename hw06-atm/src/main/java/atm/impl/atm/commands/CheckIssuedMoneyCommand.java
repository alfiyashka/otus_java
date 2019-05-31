package atm.impl.atm.commands;

import atm.ICash;
import atm.impl.atms.AbstractAtm;

import java.util.NoSuchElementException;

public class CheckIssuedMoneyCommand extends AtmCommand {

    private final int money;
    private final AbstractAtm atm;

    public CheckIssuedMoneyCommand(int money, AbstractAtm atm){
        this.money = money;
        this.atm = atm;
    }

    private void checkMultiple(int money)
    {
        int minNominalValue = atm.cells().stream().mapToInt(v -> v.nominal().getValue())
                .min().orElseThrow(NoSuchElementException::new);
        if (money % minNominalValue != 0) {
            throw new RuntimeException("Money should be multiple to " + minNominalValue);
        }
    }

    @Override
    public ICash perform () {
        checkMultiple(money);
        int atmBalance = atm.balance();
        if (money > atmBalance) {
            throw new RuntimeException("Cannot issue money. There are not enough money. Atm balance is "
                    + atmBalance + ". You tried to issue: " + money);
        }
        return checkNext();
    }
}

