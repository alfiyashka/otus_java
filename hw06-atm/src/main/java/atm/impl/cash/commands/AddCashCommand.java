package atm.impl.cash.commands;

import atm.ICashCommand;
import atm.impl.Banknote;

import java.util.Optional;
import java.util.Set;

public class AddCashCommand implements ICashCommand {
    private final Banknote banknote;
    private final Set<Banknote> banknotes;

    public AddCashCommand(Banknote banknote, Set<Banknote> banknotes ){
        this.banknote = banknote;
        this.banknotes = banknotes;
    }

    @Override
    public void execute() {
        Optional<Banknote> oldBanknote = banknotes.stream()
                .filter(it -> it.nominal().equals(banknote.nominal()))
                .findAny();
        if (!oldBanknote.isPresent()){
            banknotes.add(banknote);
        }
        else {
            Integer countOfNominal = oldBanknote.get().amount();
            Integer totalCount = countOfNominal + banknote.amount();
            banknotes.remove(oldBanknote);
            banknotes.add(new Banknote(banknote.nominal(), totalCount));
        }

    }
}
