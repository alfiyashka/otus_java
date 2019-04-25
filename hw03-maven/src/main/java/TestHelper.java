import annotations.AfterEach;
import annotations.BeforeEach;
import annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class TestHelper {

    private Class<?> testClass;

    TestHelper(Class<?> testClass) {
        this.testClass = testClass;
    }

    private boolean invokeByAnnotation(Object testObject, Class<? extends Annotation> annotation) {
        Method[] methods = testClass.getMethods();
        for (Method method: methods) {
            if (!method.isAnnotationPresent(annotation)) {
                continue;
            }
            final boolean invokeResult = invokeMethod(testObject, method, BeforeEach.class);
            if (!invokeResult && annotation == BeforeEach.class) {
                return false;
            }
        }
        return true;
    }

    private boolean invokeMethod(Object testObject, Method method, Class<? extends Annotation> annotation) {
        try {
            method.invoke(testObject);
            return true;
        }
        catch (Exception e) {
            final String error = e.getMessage() == null ? e.getCause().getMessage() : e.getMessage();
            System.out.println("Cannot perform following method with name"
                    + method.getName() + " with annotation '" + annotation.toString()
                    + "' because of following error : " + error);
            return false;
        }
    }

    private Object newInstance() {
        try {
            return testClass.getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            final String error = e.getMessage() == null ? e.getCause().getMessage() : e.getMessage();
            System.out.println("Cannot create instance of class "
                    + testClass.getName() + " because of following error : " + error);
            return null;
        }
    }

    public void performTests() {
        Method[] methods = testClass.getMethods();
        Object testObject = newInstance();
        if (testObject == null){
            return;
        }
        for (Method method : methods) {

            if (!method.isAnnotationPresent(Test.class)) {
                continue;
            }

            if (invokeByAnnotation(testObject, BeforeEach.class)) {
                invokeMethod(testObject, method, Test.class);
            }
            invokeByAnnotation(testObject, AfterEach.class);
        }
    }


}
