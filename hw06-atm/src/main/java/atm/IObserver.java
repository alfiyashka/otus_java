package atm;

public interface IObserver {
    void undoState();
    void saveState();
}
