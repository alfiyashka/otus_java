package atm.impl.atms;

import atm.IAtmVisitor;
import atm.ICash;
import atm.Nominal;
import atm.impl.atm.commands.AtmCommand;
import atm.impl.atm.commands.CheckDepositMoneyCommand;
import atm.impl.atm.commands.DepositMoneyCommand;
import atm.impl.cell.Cell;

public class RecycleAtm extends AbstractAtm {
    private final int MAX_BALANCE;

    public RecycleAtm() {
        // fill atm halfway
        int banknotesAmount = Cell.MAX_BANKNOTES_AMOUNT / 2;
        cells.add(new Cell(Nominal.RUB_100, banknotesAmount));
        cells.add(new Cell(Nominal.RUB_500, banknotesAmount));
        cells.add(new Cell(Nominal.RUB_1000, banknotesAmount));
        cells.add(new Cell(Nominal.RUB_2000, banknotesAmount));
        cells.add(new Cell(Nominal.RUB_5000, banknotesAmount));
        MAX_BALANCE = cells.stream().mapToInt(it -> it.nominal().getValue() * Cell.MAX_BANKNOTES_AMOUNT).sum();

    }

    public RecycleAtm(int nominalAmountInCell1,
                      int nominalAmountInCell2,
                      int nominalAmountInCell3,
                      int nominalAmountInCell4,
                      int nominalAmountInCell5) {
        checkNominalAmount(nominalAmountInCell1,
                nominalAmountInCell2,
                nominalAmountInCell3,
                nominalAmountInCell4,
                nominalAmountInCell5);

        cells.add(new Cell(Nominal.RUB_100, nominalAmountInCell1));
        cells.add(new Cell(Nominal.RUB_500, nominalAmountInCell2));
        cells.add(new Cell(Nominal.RUB_1000, nominalAmountInCell3));
        cells.add(new Cell(Nominal.RUB_2000, nominalAmountInCell4));
        cells.add(new Cell(Nominal.RUB_5000, nominalAmountInCell5));
        MAX_BALANCE = cells.stream().mapToInt(it -> it.nominal().getValue() * it.amount()).sum();
    }

    public RecycleAtm(RecycleAtm atm) {
        cells.addAll(atm.cells);
        MAX_BALANCE = atm.maxAvailableBalance();
    }

    @Override
    public void depositMoney(ICash cash) {
        Memento memento = new Memento(cells);
        System.out.println("Depositing " + cash.balance() + " rubles");
        try {

            AtmCommand depositMoneyCommand = new DepositMoneyCommand(cash, this);
            depositMoneyCommand.linkWith(new CheckDepositMoneyCommand(cash, this)).perform();
            System.out.println("Deposited " + cash.balance() + " rubles");
        }
        catch (Exception e) {
            undoState(memento);
            System.err.println("Following error occurred in the depositing money process: " + e.getMessage());
        }
    }

    @Override
    public int maxAvailableBalance() {
        return MAX_BALANCE;
    }

    @Override
    public RecycleAtm copy() {
        return new RecycleAtm(this);
    }

    @Override
    public void accept(IAtmVisitor v) {
        v.visitAtmBalance(this);
    }
}

