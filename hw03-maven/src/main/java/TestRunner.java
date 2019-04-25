public class TestRunner {
    private static void run(Class<?> testClass) {
        ITestHandler objectCreator = new TestHandler(testClass);

        try {
            objectCreator.run();
        }
        catch (Exception e) {
            System.out.println("Cannot run test class with name "  + testClass.getName()
                    + " because of following error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TestRunner.run(TestClass.class);
    }
}
