package atm;

import atm.impl.atms.AbstractAtm;

public interface IAtmVisitor {
    int visitAtmBalance(AbstractAtm atm);
}
