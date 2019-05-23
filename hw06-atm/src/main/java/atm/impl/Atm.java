package atm.impl;

import atm.IAtm;
import atm.Nominal;

import java.util.*;

public class Atm implements IAtm {
    private final List<Cell> cells = new ArrayList<>(5);
    private final int MAX_BALANCE;

    public Atm() {
        // fill atm halfway
        int banknotesAmount = Cell.MAX_BANKNOTES_AMOUNT / 2;
        cells.add(new Cell(Nominal.RUB_100, banknotesAmount));
        cells.add(new Cell(Nominal.RUB_500, banknotesAmount));
        cells.add(new Cell(Nominal.RUB_1000, banknotesAmount));
        cells.add(new Cell(Nominal.RUB_2000, banknotesAmount));
        cells.add(new Cell(Nominal.RUB_5000, banknotesAmount));
        MAX_BALANCE = cells.stream().mapToInt(it -> it.nominalValue() * Cell.MAX_BANKNOTES_AMOUNT).sum();
        sort();
    }

    @Override
    public void fillCells() {
        cells.forEach(Cell::fill);
    }

    private void sort() {
        cells.sort(Comparator.reverseOrder());
    }

    private void checkMultiple(int money) {
        int celWithMinNominalValue = cells.get(cells.size() - 1).nominalValue();
        if (money % celWithMinNominalValue != 0) {
            throw new RuntimeException("Money should be multiple to " + celWithMinNominalValue);
        }
    }

    public static int calculateTotalMoney(Map<Nominal, Integer> moneyInBanknotes) {
        return moneyInBanknotes.entrySet().stream().mapToInt( e -> e.getKey().getValue() * e.getValue()).sum();
    }

    private void checkDepositedMoney(Map<Nominal, Integer> moneyInBanknotes) {
        int atmBalance = balance();
        int totalMoney = calculateTotalMoney(moneyInBanknotes);
        if (totalMoney + atmBalance > MAX_BALANCE) {
            throw new RuntimeException("Cannot deposit money: atm is full.");
        }
        for (final Map.Entry<Nominal, Integer> money : moneyInBanknotes.entrySet()) {
            Cell cell = cells.stream().filter(it ->  it.nominalValue() == money.getKey().getValue())
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Cannot deposit money: unsupported banknote value "
                            + money.getKey().getValue()));
            if (money.getValue() + cell.amount() > Cell.MAX_BANKNOTES_AMOUNT) {
                throw new RuntimeException("Cannot deposit money: cell of banknote value "
                        + money.getKey().getValue() + " is full");
            }
        }
    }

    @Override
    public void depositMoney(Map<Nominal, Integer> moneyInBanknotes) {
        System.out.println("Depositing " + calculateTotalMoney(moneyInBanknotes) + " rubles");
        checkDepositedMoney(moneyInBanknotes);
        for (final Map.Entry<Nominal, Integer> money : moneyInBanknotes.entrySet()) {
            Cell cell = cells.stream().filter(it ->  it.nominalValue() == money.getKey().getValue())
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Cannot deposit money: unsupported banknote value"
                            + money.getKey().getValue()));

            cell.add(money.getValue());
        }
        System.out.println("Deposited " + calculateTotalMoney(moneyInBanknotes) + " rubles");
    }

    private void checkIssuedMoney(int money) {
        checkMultiple(money);
        int atmBalance = balance();
        if (money > atmBalance) {
            throw new RuntimeException("Cannot issue money. There are not enough money. Atm balance is "
                    + atmBalance + ". You tried to issue: " + money);
        }
    }
    @Override
    public void issueMoney(int money) {
        System.out.println("Issuing money " + money + " rubles. ");
        checkIssuedMoney(money);
        HashMap<Integer, Integer> nominalAmountMap = new HashMap<>();
        for (Cell cell: cells) {
            if (money == 0) {
                break;
            }
            int amountBanknotes = money / cell.nominalValue();
            if (amountBanknotes > 0) {
                money -= cell.issue(amountBanknotes);
                nominalAmountMap.put(cell.nominalValue(), amountBanknotes);
            }
        }
        if (money > 0 || money < 0) {
            throw new RuntimeException("Atm internal error: cannot issue money");
        }
        else {
            nominalAmountMap.forEach((k,v) -> { System.out.println(v + " banknotes of '"
                    + k + "' rubles were issued");});

        }
    }

    @Override
    public int balance() {
        return cells.stream().mapToInt(Cell::balance).sum();
    }

    @Override
    public int maxAvailableBalance() {
        return MAX_BALANCE;
    }
}
