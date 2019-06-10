package atm;


public interface IAtm {
    void fillCells();
    void depositMoney(ICash cash);
    ICash issueMoney(int money);
    int balance();
    int maxAvailableBalance();
}
