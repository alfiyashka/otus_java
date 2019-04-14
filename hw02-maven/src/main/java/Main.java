import java.util.*;

public class Main {

    private static <T> void checkGenerics (T[] dataArray, T[] zeroArray ) {

        List<T> dIYArrayList = new DIYarrayList<>();
        System.out.println("\nInitial size before adding - " + dIYArrayList.size());
        Collections.addAll(dIYArrayList, dataArray);
        System.out.println("New size after adding - " + dIYArrayList.size());
        System.out.println("All elements:");
        dIYArrayList.forEach(it -> { System.out.print("'" + it.toString()+ "' ");});

        Collections.sort((DIYarrayList)dIYArrayList);
        System.out.println("\nAfter sorting elements:");
        dIYArrayList.forEach(it -> { System.out.print("'" + it.toString()+ "' ");});

        List<T> copyList = new DIYarrayList<>(dIYArrayList.size());
        Collections.addAll(copyList, zeroArray);
        Collections.copy(copyList, dIYArrayList);

        System.out.println("\nAfter copying elements:");
        copyList.forEach(it -> { System.out.print("'" + it.toString()+ "' ");});
    }

    public static void main(String[] args) {
        Integer[] integers = new Integer[] { 10000, 200, 300, 30, 40, 50, 601, 70, 80, 90,
                1, 11, 120, 130, 14, 150, 1600, 970, 18, 190 };
        Integer[] zeroIntegers = new Integer[20];
        Arrays.fill(zeroIntegers, 0);
        checkGenerics(integers, zeroIntegers);

        String[] strings = new String[] { "countertop", "table", "chair", "door", "window", "oven", "refrigerator", "sofa",
                "tv", "commode", "labtop", "book", "towel", "keys", "mirror", "plate", "dish", "pan", "fork", "spoon" };
        String[] zeroStrings = new String[20];
        Arrays.fill(zeroStrings,"");
        checkGenerics(strings, zeroStrings);

    }
}
