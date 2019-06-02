package com.avalieva.gc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OutOfMemory {
    // for little heap 512m, 1024m
    public void generateOOM() throws InterruptedException {
        List<String> array = new ArrayList<>();
        for (int loop = 0; loop < Integer.MAX_VALUE; loop++) {
            int local = 1000000;
            for (int ind = 0; ind < local; ind ++) {
                array.add(new String(new char[1]));

            }
            for (int ind = 0; ind < local / 1000; ind ++) {
                array.remove(ind);
            }

            Thread.sleep(500);
        }
    }

    // for big heap 8192m
    public void generateOOM1() throws InterruptedException {
        List<String> array = new ArrayList<>();
        for (int loop = 0; loop < Integer.MAX_VALUE; loop++) {
            int local = 120000000;
            for (int ind = 0; ind < local; ind ++) {
                array.add(new String(new char[1]));

            }
            for (int ind = 0; ind < local / 100000; ind ++) {
                array.remove(ind);
            }
        }
    }

    // for  big heap 20480m
    public void generateOOM2() throws InterruptedException {
        List<String> array = new ArrayList<>();
        for (int loop = 0; loop < Integer.MAX_VALUE; loop++) {
            Integer local = 1000000000; //1) 1000000;
            for (int ind = 0; ind < local; ind ++) {
                array.add(new String(new char[1]));

            }
            for (int ind = 0; ind < 50000000; ind ++) {//local / 20000000
                array.remove(ind);
            }
        }
    }

    public static void main(String[] arg) {
        long start = System.nanoTime();
        String currentTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println("current time " + currentTimeStamp);

        try {
            OutOfMemory outOfMemory = new OutOfMemory();
            outOfMemory.generateOOM2();
        }
        catch (OutOfMemoryError e) {
            System.out.println("!!!!!!!!!!!!Exception:" + e.getMessage());
            long duration = System.nanoTime() - start;
            System.out.println("duration " + duration/1000000000);
        }
        catch (Exception e) {
            System.err.println("error " + e.getMessage());
        }
    }
}
