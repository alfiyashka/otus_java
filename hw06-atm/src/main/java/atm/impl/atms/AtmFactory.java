package atm.impl.atms;

// Factory pattern
public class AtmFactory {
    public AbstractAtm getAtm(String atmType) {
        AbstractAtm atm = null;
        if (atmType.equals("recycle")) {
            atm = new RecycleAtm();
        } else if (atmType.equals("cashOut")) {
            atm = new CashOutAtm();
        } else if (atmType.equals("cashInOut")) {
            atm = new CashInCashOutAtm();
        }
        return atm;
    }

    public AbstractAtm getAtm(String atmType,
                              int nominalAmountInCell1,
                              int nominalAmountInCell2,
                              int nominalAmountInCell3,
                              int nominalAmountInCell4,
                              int nominalAmountInCell5) {
        AbstractAtm atm = null;
        if (atmType.equals("recycle")) {
            atm = new RecycleAtm(nominalAmountInCell1,
                    nominalAmountInCell2,
                    nominalAmountInCell3,
                    nominalAmountInCell4,
                    nominalAmountInCell5);
        } else if (atmType.equals("cashOut")) {
            atm = new CashOutAtm(nominalAmountInCell1,
                    nominalAmountInCell2,
                    nominalAmountInCell3,
                    nominalAmountInCell4,
                    nominalAmountInCell5);
        } else if (atmType.equals("cashInOut")) {
            atm = new CashInCashOutAtm(nominalAmountInCell1,
                    nominalAmountInCell2,
                    nominalAmountInCell3,
                    nominalAmountInCell4,
                    nominalAmountInCell5);
        }
        return atm;
    }
}
