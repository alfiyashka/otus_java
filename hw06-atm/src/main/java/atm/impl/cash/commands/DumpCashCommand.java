package atm.impl.cash.commands;

import atm.ICashCommand;
import atm.impl.Banknote;

import java.util.Set;

public class DumpCashCommand implements ICashCommand {

    private final Set<Banknote> banknotes;
    public DumpCashCommand(Set<Banknote> banknotes ){
        this.banknotes = banknotes;
    }

    @Override
    public void execute() {
        StringBuilder dumpMess = new StringBuilder();
        dumpMess.append("In cash: ");
        if (banknotes.isEmpty()) {
            dumpMess.append("0 rubles. ");
        }
        else {
            banknotes.forEach((it) -> {
                dumpMess.append(it.amount() + " banknotes of '"
                        + it.nominal().getValue() + "' rubles; ");
            });
        }
        System.out.println(dumpMess.toString());
    }
}
