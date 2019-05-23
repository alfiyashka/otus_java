package atm;

import atm.impl.Atm;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        IAtm atm = new Atm();

        System.out.println("Initial balance of atm " + atm.balance() + " rubles");

        int issuedMoney = 1200;
        atm.issueMoney(issuedMoney);
        System.out.println("Issued money : " + issuedMoney + ". Balance of atm " + atm.balance() + " rubles");

        int issuedMoney2 = 1123200;
        atm.issueMoney(issuedMoney2);
        System.out.println("Issued money : " + issuedMoney2 + ". Balance of atm " + atm.balance() + " rubles");

        Map<Nominal, Integer> depositMoney = new HashMap<>();
        depositMoney.put(Nominal.RUB_1000, 20);
        depositMoney.put(Nominal.RUB_2000, 10);

        atm.depositMoney(depositMoney);
        System.out.println("Deposited money : " + Atm.calculateTotalMoney(depositMoney)
                + ". Balance of atm " + atm.balance() + " rubles");

        try {
            Map<Nominal, Integer> depositMoneyIncorrectNominal = new HashMap<>();
            depositMoneyIncorrectNominal.put(Nominal.RUB_50, 20);
            depositMoneyIncorrectNominal.put(Nominal.RUB_2000, 10);
            atm.depositMoney(depositMoneyIncorrectNominal);
            System.out.println("Deposited money : " + Atm.calculateTotalMoney(depositMoney)
                    + ". Balance of atm " + atm.balance() + " rubles");
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage()
                    + ". Balance of atm " + atm.balance() + " rubles");
        }

        try {
            int getMoneyExcess = atm.maxAvailableBalance() + 100;
            atm.issueMoney(getMoneyExcess);
            System.out.println("Issued money : " + getMoneyExcess
                    + ". Balance of atm " + atm.balance() + " rubles");
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }

        try {
            int getMoneyExcess = 999;
            atm.issueMoney(getMoneyExcess);
            System.out.println("Issued money : " + getMoneyExcess + ". Balance of atm " + atm.balance());
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }
    }
}
