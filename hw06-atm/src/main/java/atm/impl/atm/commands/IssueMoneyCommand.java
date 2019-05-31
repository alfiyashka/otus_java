package atm.impl.atm.commands;

import atm.ICash;
import atm.IIssueMoneyAlgorithm;

public class IssueMoneyCommand extends AtmCommand {

    private final int money;
    private final IIssueMoneyAlgorithm algorithm;
    public IssueMoneyCommand(int money, IIssueMoneyAlgorithm algorithm){
        this.money = money;
        this.algorithm = algorithm;
    }

    @Override
    public ICash perform () {
        algorithm.issueMoney(money);
        return checkNext();
    }

}
