package cn.howardliu.demo.apm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * <br>created at 17-3-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ApmClassFileTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (loader == null
                || ApmFilter.isNotNeedInjectClassLoader(loader.getClass().getName())
                || ApmFilter.isNotNeedInject(className)) {
            return classfileBuffer;
        }
        try {
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            reader.accept(new ApmClassAdapter(writer, className), 0);
            return writer.toByteArray();
        } catch (Throwable e) {
            e.printStackTrace();
            return classfileBuffer;
        }
    }
}
