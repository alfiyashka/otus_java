package atm.impl.atms;

import atm.IAtmVisitor;
import atm.ICash;
import atm.IIssueMoneyAlgorithm;
import atm.impl.cell.CashInCell;

//decorator
public class CashInCashOutAtm extends AbstractAtm {
    private CashOutAtm cashOutAtm;
    private CashInCell cashInCell;
    private CashInOutMemento cashInOutMemento;

    public CashInCashOutAtm() {
        cashOutAtm = new CashOutAtm();
        cashInCell = new CashInCell();
    }

    public CashInCashOutAtm(int nominalAmountInCell1,
                            int nominalAmountInCell2,
                            int nominalAmountInCell3,
                            int nominalAmountInCell4,
                            int nominalAmountInCell5) {
        checkNominalAmount(nominalAmountInCell1,
                nominalAmountInCell2,
                nominalAmountInCell3,
                nominalAmountInCell4,
                nominalAmountInCell5);

        cashOutAtm = new CashOutAtm(nominalAmountInCell1,
                nominalAmountInCell2,
                nominalAmountInCell3,
                nominalAmountInCell4,
                nominalAmountInCell5);
        cashInCell = new CashInCell();
    }

    public CashInCashOutAtm(CashInCashOutAtm atm) {
        cashOutAtm = atm.cashOutAtm().copy();
        cashInCell = atm.cashInCell;
    }

    public CashOutAtm cashOutAtm() {
        return cashOutAtm();
    }

    public CashInCell cashInCell() {
        return cashInCell;
    }

    @Override
    public void fillCells() {
        cashOutAtm.fillCells();
    }

    @Override
    public void depositMoney(ICash cash) {
        CashInOutMemento memento = new CashInOutMemento(cashOutAtm, cashInCell);
        System.out.println("Depositing " + cash.balance() + " rubles");
        try {
            cash.getBanknotes().forEach( it -> cashInCell.add(it));
            System.out.println("Deposited " + cash.balance() + " rubles");
        }
        catch (Exception e) {
            undoState(memento);
            System.err.println("Following error occurred in the depositing money process: " + e.getMessage());
        }
    }

    @Override
    public ICash issueMoney(int money) {
        return cashOutAtm.issueMoney(money);
    }

    @Override
    public int balance() {
        return cashOutAtm.balance() + cashInCell.balance();
    }

    @Override
    public int maxAvailableBalance() {
        return cashOutAtm.maxAvailableBalance();
    }

    @Override
    public void setIssueMoneyAlgorithm(IIssueMoneyAlgorithm issueMoneyAlgorithm) {
        cashOutAtm.setIssueMoneyAlgorithm(issueMoneyAlgorithm);
    }

    @Override
    public void accept(IAtmVisitor v) {
        v.visitAtmBalance(this);
    }

    @Override
    public CashInCashOutAtm copy() {
        return new CashInCashOutAtm(this);
    }

    protected class CashInOutMemento  {
        private final CashInCell cashInCell;
        private final CashOutAtm cashOutAtm;
        CashInOutMemento(CashOutAtm cashOutAtm, CashInCell cashInCell) {
            this.cashOutAtm = cashOutAtm.copy();
            this.cashInCell = cashInCell.copy();
        }
    }

    @Override
    public void saveState() {
        cashInOutMemento = new CashInOutMemento(cashOutAtm, cashInCell);
    }

    protected void undoState(CashInOutMemento memento) {
        cashInCell = memento.cashInCell;
        cashOutAtm = memento.cashOutAtm;
    }

    @Override
    public void undoState() {
        if (cashInOutMemento != null) {
            undoState(cashInOutMemento);
        }
        else {
            throw new RuntimeException("Cannot undo state because initial state was not set");
        }
    }
}

