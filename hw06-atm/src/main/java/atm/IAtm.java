package atm;

import java.util.Map;

public interface IAtm {
    void fillCells();
    void depositMoney(Map<Nominal, Integer> moneyInBanknotes);
    void issueMoney(int money);
    int balance();
    int maxAvailableBalance();
}
