package atm.impl.atms;

import atm.IAtmVisitor;
import atm.ICash;
import atm.Nominal;
import atm.impl.cell.Cell;

import java.util.stream.Collectors;

public class CashOutAtm extends AbstractAtm {
    private final int MAX_BALANCE;


    public CashOutAtm() {
        // fill atm totally
        cells.add(new Cell(Nominal.RUB_100, Cell.MAX_BANKNOTES_AMOUNT));
        cells.add(new Cell(Nominal.RUB_500, Cell.MAX_BANKNOTES_AMOUNT));
        cells.add(new Cell(Nominal.RUB_1000, Cell.MAX_BANKNOTES_AMOUNT));
        cells.add(new Cell(Nominal.RUB_2000, Cell.MAX_BANKNOTES_AMOUNT));
        cells.add(new Cell(Nominal.RUB_5000, Cell.MAX_BANKNOTES_AMOUNT));
        MAX_BALANCE = cells.stream().mapToInt(it -> it.nominal().getValue() * Cell.MAX_BANKNOTES_AMOUNT).sum();

    }

    public CashOutAtm(int nominalAmountInCell1,
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

    public CashOutAtm(CashOutAtm atm) {
        cells.addAll(atm.cells.stream().map(it -> new Cell(it)).collect(Collectors.toList()));
        MAX_BALANCE = atm.maxAvailableBalance();
    }

    @Override
    public void depositMoney(ICash cash) {
        throw new RuntimeException("Cannot deposit money into CashIn Atm");
    }


    @Override
    public int maxAvailableBalance() {
        return MAX_BALANCE;
    }

    @Override
    public CashOutAtm copy() {
        return new CashOutAtm(this);
    }

    @Override
    public void accept(IAtmVisitor v) {
        v.visitAtmBalance(this);
    }
}

