import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class LoggingAdapter extends AdviceAdapter {
    private final String name;
    MethodNode methodNode;
    final Type[] argumentsTypes;

    public LoggingAdapter(int api, MethodVisitor methodVisitor,
                           int access, String name, String descriptor, MethodNode methodNode ) {
        super(api, methodVisitor, access, name, descriptor);
        this.name = name;
        this.methodNode = methodNode;
        this.argumentsTypes = new Method(name, descriptor).getArgumentTypes();
    }

    private boolean functionHasArguments () {
        return argumentsTypes != null && argumentsTypes.length > 0;
    }

    private String logMessage(){
        String logMessage = "executed method: " + name + ", arguments: ";

        if (!functionHasArguments()) {
            logMessage += "none";
            return logMessage;
        }
        for (int localVariableIndex = 1; localVariableIndex < argumentsTypes.length + 1; localVariableIndex ++) {
            LocalVariableNode localVariableNode = methodNode.localVariables.get(localVariableIndex);
            if (localVariableNode == null) {
                throw new RuntimeException("Local variable with index " + localVariableIndex + " was not found");
            }
            logMessage += localVariableIndex + ") "+ localVariableNode.name + " - \u0001; ";
        }
        return logMessage;
    }

    private void visitArguments() {
        int variableIndex = 1;
        for (Type type : argumentsTypes) {
            try {
                if (type.getClassName().equals(String.class.getName())) {
                    super.visitVarInsn(Opcodes.ALOAD, variableIndex);
                } else if (type.equals(Type.INT_TYPE) ||
                        type.equals(Type.BOOLEAN_TYPE) || type.equals(Type.BYTE_TYPE)
                        || type.equals(Type.CHAR_TYPE) || type.equals(Type.SHORT_TYPE)) {
                    super.visitVarInsn(Opcodes.ILOAD, variableIndex);
                } else if (type.equals(Type.FLOAT_TYPE)) {
                    super.visitVarInsn(Opcodes.FLOAD, variableIndex);
                } else if (type.equals(Type.DOUBLE_TYPE)) {
                    super.visitVarInsn(Opcodes.DLOAD, variableIndex);
                } else {
                    System.err.println("Cannot load the variable with type '" + type.getClassName()
                            + "' from stack because of following error: Unsupported type.");
                }
                variableIndex++;
            }
            catch (Exception e) {
                System.err.println("Cannot load the variable with type '" + type.getClassName()
                        + "' from stack because of following error: " + e.getMessage());
                throw e;
            }
        }

    }

    private String makeConcatDescriptor() {
        String makeConcatDescriptor = "(";
        for (Type type : argumentsTypes) {
            makeConcatDescriptor += type.getDescriptor();
        }
        makeConcatDescriptor += ")Ljava/lang/String;";
        return makeConcatDescriptor;
    }

    @Override
    protected void onMethodEnter() {
        if (!functionHasArguments())
        {
            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn(logMessage());
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        else {
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
                    "Ljava/io/PrintStream;");

            visitArguments();

            String makeConcatHandleDescriptor = MethodType.methodType(CallSite.class, MethodHandles.Lookup.class,
                    String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString();
            Handle handle = new Handle(H_INVOKESTATIC, Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                    "makeConcatWithConstants", makeConcatHandleDescriptor, false);

            super.visitInvokeDynamicInsn("makeConcatWithConstants", makeConcatDescriptor(),
                    handle, new Object[]{ logMessage() });
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                    "(Ljava/lang/String;)V", false);
        }
    }
}

