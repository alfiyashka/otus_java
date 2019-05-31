package atm;


// strategy pattern
public interface IIssueMoneyAlgorithm {
    ICash issueMoney(int money);
}
