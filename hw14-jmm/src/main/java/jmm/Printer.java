package jmm;

public class Printer {
    private long id;
    private boolean printedThreadNames = false;
    public synchronized void print(long counter) throws InterruptedException {
        while (id == Thread.currentThread().getId()) {
            wait();
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
