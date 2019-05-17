public class MainByteCodes {


    public static void main(String[] args) {
        try {
            TestLogging testLogging = (new TestLogging());
            testLogging.calculation(1, (short)12 , 'u');
            testLogging.function2(0.34F, 0.99);
            testLogging.function3(true, (byte) 3);
            testLogging.function4();
        }
        catch (Exception e) {
            throw e;
        }
    }
}
