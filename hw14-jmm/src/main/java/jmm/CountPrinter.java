package jmm;

public class CountPrinter implements Runnable {
    Printer printer;

    CountPrinter(Printer printer) {
        this.printer = printer;
    }

    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                for (int i = 1; i < 10; i++) {
                    printer.print(i);
                }
                for (int i = 10; i > 1; i--) {
                    printer.print(i);
                }
            }
        }
        catch (InterruptedException e) {

        }

    }
}
