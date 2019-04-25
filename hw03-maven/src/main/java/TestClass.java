import annotations.AfterEach;
import annotations.BeforeEach;
import annotations.Test;

public class TestClass {
    public TestClass() {
        System.out.println("Create new object of testClass");
    }

    @BeforeEach
    public void initialize() {
        System.out.println("Before each1");
    }

    @BeforeEach
    public void prepare() {
        System.out.println("Before each2");
    }

    @AfterEach
    public void release() {
        System.out.println("After each1");
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
