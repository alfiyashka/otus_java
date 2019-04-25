import annotations.AfterEach;
import annotations.BeforeEach;
import annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestHandler implements ITestHandler {
    private Class<?> testClass;

    TestHandler(Class<?> testClass) {
        this.testClass = testClass;
    }

    private List<Method> beforeEachMethods() {
        Method[] methods = testClass.getMethods();
        List<Method> beforeEachMethods = new ArrayList<>();
        for (Method method: methods) {
            if (!method.isAnnotationPresent(BeforeEach.class)) {
                continue;
            }
            beforeEachMethods.add(method);
        }
        return beforeEachMethods;
    }

    private List<Method> afterEachMethods() {
        Method[] methods = testClass.getMethods();
        List<Method> afterEachMethods = new ArrayList<>();
        for (Method method: methods) {
            if (!method.isAnnotationPresent(AfterEach.class)) {
                continue;
            }
            afterEachMethods.add(method);
        }
        return afterEachMethods;
    }

    private void invokeBeforeEach(Object testObject) {
        List<Method> beforeEachMethods = beforeEachMethods();
        for (Method method : beforeEachMethods) {
            try {
                method.invoke(testObject);
            }
            catch (Exception e) {
                throw new RuntimeException("Cannot perform following BeforeEach method with name"
                        + method.getName() + " because of following error :" + e.getMessage(), e);
            }
        }
    }

    private void invokeAfterEach(Object testObject) {
        List<Method> afterEachMethods = afterEachMethods();
        for (Method method : afterEachMethods) {
            try {
                method.invoke(testObject);
            }
            catch (Exception e) {
                throw new RuntimeException("Cannot perform following AfterEach method with name"
                        + method.getName() + " because of following error :" + e.getMessage(), e);
            }
        }
    }

    private Object newInstance() {
        try {
            return testClass.getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot create instance of class "
                    + testClass.getName() + " because of following error :" + e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        Method[] methods = testClass.getMethods();
        for (Method method : methods) {
            try {
                if (!method.isAnnotationPresent(Test.class)) {
                    continue;
                }
                Object testObject = newInstance();
                invokeBeforeEach(testObject);
                method.invoke(testObject);
                invokeAfterEach(testObject);
            }
            catch (Exception e) {
                throw new RuntimeException("Cannot perform following Test method with name"
                        + method.getName() + " because of following error :" + e.getMessage(), e);
            }
        }
    }

}

