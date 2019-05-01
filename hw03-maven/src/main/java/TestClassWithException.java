import annotations.AfterEach;
import annotations.BeforeEach;
import annotations.Test;

public class TestClassWithException {
    public TestClassWithException() {
        System.out.println("Create new object of " + this.getClass().getName());
    }

    @BeforeEach
    public void initialize() {
        System.out.println("Before each1");
    }

    @BeforeEach
    public void prepare() {
        System.out.println("Before each2 failed");
        throw new RuntimeException("Some Error");
    }

    @AfterEach
    public void release() {
        System.out.println("After each1");
    }

    @AfterEach
    public void release2() {
        System.out.println("After each2");
    }

    @Test
    public void test1() {
        System.out.println("Test1");
    }

    @Test
    public void test2() {
        System.out.println("Test2");
    }

    @Test
    public void test3() {
        System.out.println("test3");
    }
}
