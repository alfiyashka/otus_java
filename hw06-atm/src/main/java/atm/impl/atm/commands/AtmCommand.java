package atm.impl.atm.commands;

import atm.ICash;
import atm.impl.Cash;

//chain of responsibility
public abstract class AtmCommand {
    private AtmCommand next;

    public AtmCommand linkWith(AtmCommand next) {
        this.next = next;
        return next;
    }

    public abstract ICash perform();

    protected ICash checkNext() {
        if (next == null) {
            return new Cash();
        }
        return next.perform();
    }
}
