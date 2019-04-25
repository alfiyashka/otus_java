import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

public class HelloOtus {
    private static String joinStrings(List<String> strings, String separator) {
        return Joiner.on(separator).skipNulls().join(strings);
    }

    public static void main(String[] args) {
        ArrayList fruits = new ArrayList();
        fruits.add(Fruits.Apple);
        fruits.add(Fruits.Banan);
        fruits.add(null);
        fruits.add(Fruits.Lemon);

        System.out.println("The following fruits are growing in the garden: "
                + joinStrings(fruits, ", "));
    }
}
