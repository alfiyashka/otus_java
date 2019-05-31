package atm.impl.atms;

import atm.*;
import atm.impl.Cash;
import atm.impl.IssueMoneyAlgorithm;
import atm.impl.atm.commands.AtmCommand;
import atm.impl.atm.commands.CheckIssuedMoneyCommand;
import atm.impl.atm.commands.IssueMoneyCommand;
import atm.impl.cell.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractAtm implements IAtm, ICopyable, IObserver {


    protected final List<Cell> cells = new ArrayList<>(5);

    private Memento memento;

    private IIssueMoneyAlgorithm issueMoneyAlgorithm = new IssueMoneyAlgorithm(cells);
    public AbstractAtm() {
    }

    public void setIssueMoneyAlgorithm (IIssueMoneyAlgorithm algorithm) {
        issueMoneyAlgorithm = algorithm;
    }

    public List<Cell> cells() {
        return cells;
    }

    @Override
    public void fillCells() {
        cells.forEach(Cell::fill);
    }

    @Override
    public ICash issueMoney(int money) {
        Memento mementoBeforeError = new Memento(cells);
        try {
            System.out.println("Issuing money " + money + " rubles. ");
            AtmCommand issueMoneyCommand = new CheckIssuedMoneyCommand(money,this);
            issueMoneyCommand.linkWith(new IssueMoneyCommand(money, issueMoneyAlgorithm));
            return issueMoneyCommand.perform();
        }
        catch(Exception e) {
            System.err.println("Following error occurred in the issuing money process: " + e.getMessage());
            undoState(mementoBeforeError);
            return new Cash();
        }
    }

    @Override
    public int balance() {
        return cells.stream().mapToInt(Cell::balance).sum();
    }


    protected void undoState(Memento memento) {
        cells.clear();
        cells.addAll(memento.initialCells);
    }

    protected class Memento {
        private List<Cell> initialCells = new ArrayList<>();

        Memento(List<Cell> cells) {
            initialCells.addAll(cells.stream().map(it -> new Cell(it)).collect(Collectors.toList()));
        }
    }

    @Override
    public void saveState() {
        memento = new Memento(cells);
    }

    @Override
    public void undoState() {
        if (memento != null) {
            undoState(memento);
        }
        else {
            throw new RuntimeException("Cannot undo state because initial state was not set");
        }
    }

    public abstract void accept(IAtmVisitor v);

    protected void checkNominalAmount(int nominalAmountInCell1,
                                    int nominalAmountInCell2,
                                    int nominalAmountInCell3,
                                    int nominalAmountInCell4,
                                    int nominalAmountInCell5) {
        if (nominalAmountInCell1 > Cell.MAX_BANKNOTES_AMOUNT
                || nominalAmountInCell2 > Cell.MAX_BANKNOTES_AMOUNT
                || nominalAmountInCell3 > Cell.MAX_BANKNOTES_AMOUNT
                || nominalAmountInCell4 > Cell.MAX_BANKNOTES_AMOUNT
                || nominalAmountInCell5 > Cell.MAX_BANKNOTES_AMOUNT) {
            throw new RuntimeException("One of cell size value is bigger than available max size value "
                    + Cell.MAX_BANKNOTES_AMOUNT);
        }

        if (nominalAmountInCell1 < 0
                || nominalAmountInCell2 < 0
                || nominalAmountInCell3 < 0
                || nominalAmountInCell4 < 0
                || nominalAmountInCell5 < 0) {
            throw new RuntimeException("One of cell size value is negative value");
        }

    }

}
