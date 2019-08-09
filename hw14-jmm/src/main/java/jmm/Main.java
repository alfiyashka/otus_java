package jmm;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        Printer printer = new Printer();
        CountPrinter countPrinter1 = new CountPrinter(printer);
        CountPrinter countPrinter2 = new CountPrinter(printer);
        Thread thread1 = new Thread(countPrinter1);
        Thread thread2 = new Thread(countPrinter2);

        thread1.start();
        thread2.start();

        Thread.sleep(1000);

       thread1.interrupt();
       thread2.interrupt();
    }
}
