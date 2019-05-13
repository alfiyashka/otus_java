import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String agentArgument, Instrumentation instrumentation) {
        System.out.println("Agent run");
        instrumentation.addTransformer(new ClassTransformer());
    }
}
