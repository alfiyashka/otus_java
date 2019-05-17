import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

public class ClassTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {
        ClassReader reader = new ClassReader(classfileBuffer);

        final ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);

        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7, writer) {
            public String className;

            @Override
            public void visit(int version, int access, String name, String signature,
                              String superName, String[] interfaces) {

                className = name;
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc,
                                             String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

                try {

                    MethodNode methodNode = classNode.methods.stream()
                            .filter(it -> it.name.equals(name)).findFirst().get();

                    if (methodNode.visibleAnnotations == null) {
                        return methodVisitor;
                    }
                    List<AnnotationNode> necessaryLogAnnotation = methodNode.visibleAnnotations.stream()
                            .filter(it -> it.desc.equals("LLog;")).collect(Collectors.toList());
                    if (necessaryLogAnnotation.isEmpty()) {
                        return methodVisitor;
                    }
                    return new LoggingAdapter(Opcodes.ASM7, methodVisitor, access, name, desc, methodNode);
                } catch (Exception e) {
                    return methodVisitor;
                }
            }
        };
        reader.accept(visitor, Opcodes.ASM7);
        return writer.toByteArray();
    }
}
