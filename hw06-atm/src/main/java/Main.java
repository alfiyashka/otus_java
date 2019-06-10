import atm.IAtm;
import atm.ICash;
import atm.Nominal;
import atm.impl.Banknote;
import atm.impl.Cash;
import atm.impl.Department;
import atm.impl.atms.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static void workWithSingleRecycleAtm() {
        IAtm atm = new AtmFactory().getAtm("recycle");

        System.out.println("Initial balance of recycle atm " + atm.balance() + " rubles");

        int issuedMoney= 1123200;
        ICash cashIssued = atm.issueMoney(issuedMoney);
        System.out.println("Issued money : " + cashIssued.balance() + " rubles. Balance of atm " + atm.balance() + " rubles");
        cashIssued.dump();

        ICash depositCash = new Cash();
        depositCash.add(new Banknote(Nominal.RUB_1000, 20));
        depositCash.add(new Banknote(Nominal.RUB_2000, 10));

        atm.depositMoney(depositCash);
        System.out.println("Deposited money : " + depositCash.balance()
                + ". Balance of atm " + atm.balance() + " rubles");

        try {
            ICash depositCashIncorrectNominal = new Cash();
            depositCashIncorrectNominal.add(new Banknote(Nominal.RUB_50, 20));
            depositCashIncorrectNominal.add(new Banknote(Nominal.RUB_2000, 10));
            atm.depositMoney(depositCashIncorrectNominal);
            System.out.println("Deposited money : " + depositCashIncorrectNominal.balance()
                    + ". Balance of atm " + atm.balance() + " rubles");
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage()
                    + ". Balance of atm " + atm.balance() + " rubles");
        }

        try {
            int issueMoneyExcess = atm.maxAvailableBalance() + 100;
            ICash cash = atm.issueMoney(issueMoneyExcess);
            System.out.println("Issued money : " + cash.balance()
                    + " rubles. Balance of atm " + atm.balance() + " rubles");
            cash.dump();
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }

        try {
            int getMoneyExcess = 999;
            ICash cash = atm.issueMoney(getMoneyExcess);
            System.out.println("Issued money : " + cash.balance() + " rubles. Balance of atm " + atm.balance());
            cash.dump();
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }
    }

    private static void workWithSingleCashOutAtm() {
        IAtm atm =  new AtmFactory().getAtm("cashOut");

        System.out.println("Initial balance of cashIn atm " + atm.balance() + " rubles");

        try {
            int issuedMoney = 12000;
            ICash cashIssued = atm.issueMoney(issuedMoney);
            System.out.println("Issued money : " + cashIssued.balance()
                    + " rubles . Balance of atm " + atm.balance() + " rubles");
            cashIssued.dump();
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage()
                    + ". Balance of atm " + atm.balance() + " rubles");
        }

        try {
            ICash depositCash = new Cash();
            depositCash.add(new Banknote(Nominal.RUB_1000, 20));
            depositCash.add(new Banknote(Nominal.RUB_2000, 10));

            atm.depositMoney(depositCash);
            System.out.println("Deposited money : " + depositCash.balance()
                    + ". Balance of atm " + atm.balance() + " rubles");
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage()
                    + ". Balance of atm " + atm.balance() + " rubles");
        }

        try {
            int issueMoneyExcess = atm.maxAvailableBalance() + 100;
            ICash cash = atm.issueMoney(issueMoneyExcess);
            System.out.println("Issued money : " + cash.balance()
                    + " rubles. Balance of atm " + atm.balance() + " rubles");
            cash.dump();
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }

        try {
            int getMoneyExcess = 999;
            ICash cash = atm.issueMoney(getMoneyExcess);
            System.out.println("Issued money : " + cash.balance() + " rubles. Balance of atm " + atm.balance());
            cash.dump();
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }
    }

    private static void workWithSingleCashInOutAtm() {
        IAtm atm =  new AtmFactory().getAtm("cashInOut");

        System.out.println("Initial balance of cashInCashOut atm " + atm.balance() + " rubles");

        try {
            int issuedMoney = 15500;
            ICash cashIssued = atm.issueMoney(issuedMoney);
            System.out.println("Issued money : " + cashIssued.balance()
                    + " rubles . Balance of atm " + atm.balance() + " rubles");
            cashIssued.dump();
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage()
                    + ". Balance of atm " + atm.balance() + " rubles");
        }

        try {
            ICash depositCash = new Cash();
            depositCash.add(new Banknote(Nominal.RUB_1000, 20));
            depositCash.add(new Banknote(Nominal.RUB_2000, 10));
            depositCash.add(new Banknote(Nominal.RUB_100, 10));

            atm.depositMoney(depositCash);
            System.out.println("Deposited money : " + depositCash.balance()
                    + ". Balance of atm " + atm.balance() + " rubles");
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage()
                    + ". Balance of atm " + atm.balance() + " rubles");
        }

        try {
            int issueMoneyExcess = atm.maxAvailableBalance() + 100;
            ICash cash = atm.issueMoney(issueMoneyExcess);
            System.out.println("Issued money : " + cash.balance()
                    + " rubles. Balance of atm " + atm.balance() + " rubles");
            cash.dump();
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }

        try {
            int getMoneyExcess = 999;
            ICash cash = atm.issueMoney(getMoneyExcess);
            System.out.println("Issued money : " + cash.balance() + " rubles. Balance of atm " + atm.balance());
            cash.dump();
        }
        catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        }
    }

    private static void workWithDepartment() {
        AtmFactory atmFactory = new AtmFactory();
        List<CashOutAtm> cashOutAtms = new ArrayList<>();
        CashOutAtm cashOutAtm = new CashOutAtm();
        cashOutAtms.add(cashOutAtm);
        cashOutAtms.add(cashOutAtm.copy());
        AbstractAtm cashOutAtmWithDiffCells = atmFactory.getAtm("cashOut", 1000,
                100, 10, 50, 20);
        cashOutAtms.add((CashOutAtm) cashOutAtmWithDiffCells);

        List<CashInCashOutAtm> cashInCashOutAtms = new ArrayList<>();
        AbstractAtm cashInOutAtmWithDiffCells = atmFactory.getAtm("cashInOut", 1000,
                100, 1000, 150, 20);
        cashInCashOutAtms.add((CashInCashOutAtm) cashInOutAtmWithDiffCells);

        List<RecycleAtm> recycleAtms = new ArrayList<>();
        RecycleAtm recycleAtm = new RecycleAtm();
        recycleAtms.add(recycleAtm);
        recycleAtms.add(recycleAtm.copy());

        Department.Builder departmentBuilder = new Department.Builder();
        Department department = departmentBuilder
                .withDepartmentName("Department")
                .withCashInCachOutAtms(cashInCashOutAtms)
                .withCashOutAtms(cashOutAtms)
                .withRecycleAtms(recycleAtms)
                .build();

        System.out.println("Department balance is " + department.balance() + " rubles");

        // change state
        cashInOutAtmWithDiffCells.issueMoney(1000);
        ICash depositCash = new Cash();
        depositCash.add(new Banknote(Nominal.RUB_1000, 20));
        depositCash.add(new Banknote(Nominal.RUB_2000, 10));
        cashInOutAtmWithDiffCells.depositMoney(depositCash);
        cashOutAtm.issueMoney(9000);
        recycleAtm.issueMoney(1200);

        System.out.println("Department balance is " + department.balance() + " rubles after money operations");

        department.undoStateAtms();

        System.out.println("Department balance is " + department.balance() + " rubles after undo state to initial");
    }

    public static void main(String[] args) {
        workWithSingleRecycleAtm();
        workWithSingleCashOutAtm();
        workWithSingleCashInOutAtm();
        workWithDepartment();
    }

}
