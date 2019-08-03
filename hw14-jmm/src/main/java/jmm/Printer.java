package jmm;

public class Printer {
    private long id;

    public synchronized void print(long counter) {
        boolean printedThreadNames = false;
        while (id == Thread.currentThread().getId()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!printedThreadNames) {
                printedThreadNames = true;
            }
        }
        if (!printedThreadNames) {
            System.out.println(Thread.currentThread().getName() + " : ");
        }
        id = Thread.currentThread().getId();
        System.out.println(counter);
        notify();
    }
}
