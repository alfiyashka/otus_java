public class TestRunner {
    public static void run(Class<?> testClass) {
        new TestHelper(testClass).performTests();
    }
}
