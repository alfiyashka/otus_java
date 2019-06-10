package atm.impl;

import atm.ICash;
import atm.IIssueMoneyAlgorithm;
import atm.impl.cell.Cell;

import java.util.Comparator;
import java.util.List;

// strategy pattern
public class IssueMoneyAlgorithm implements IIssueMoneyAlgorithm {
    private final List<Cell> cells;

    public IssueMoneyAlgorithm(List<Cell> cells) {
        this.cells = cells;
    }

    private void sort() {
        cells.sort(Comparator.reverseOrder());
    }


    @Override
    public ICash issueMoney(int money) {
        int issueMoney = money;
        sort();

        ICash cash = new Cash();
        for (Cell cell: cells) {
            if (issueMoney == 0) {
                break;
            }
            int amountBanknotes = issueMoney / cell.nominal().getValue();
            if (amountBanknotes > 0) {
                issueMoney -= cell.issue(amountBanknotes);
                cash.add(new Banknote(cell.nominal(), amountBanknotes));
            }
        }
        if (issueMoney > 0 || issueMoney < 0) {
            throw new RuntimeException("Atm internal error: cannot issue money");
        }
        else {
            System.out.println("Issued money " + money + " rubles. ");
            return cash;
        }
    }
}
