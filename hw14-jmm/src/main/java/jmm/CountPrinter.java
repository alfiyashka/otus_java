package jmm;

public class CountPrinter implements Runnable {
    Printer printer;

    CountPrinter(Printer printer) {
        this.printer = printer;
    }

    public void run() {
        while (true) {
            for (int i = 1; i < 10; i++) {
                printer.print(i);
            }
            for (int i = 10; i > 1; i--) {
                printer.print(i);
            }
        }
    }
}