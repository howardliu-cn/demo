package cn.howardliu.demo.apm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * <br>created at 17-3-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ApmClassAdapter extends ClassAdapter {
    private String className;
    private String fileName;

    public ApmClassAdapter(ClassVisitor classVisitor, String className) {
        super(classVisitor);
        this.className = className;
        ApmCounter.classCount.getAndIncrement();
    }

    @Override
    public void visitSource(final String source, final String debug) {
        super.visitSource(source, debug);
        this.fileName = source;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int arg, String name, String descriptor, String signature, String[] exceptions) {
        // 静态区域不注入
        if ("<clinit>".equals(name) || "<init>".equals(name)) {
            return super.visitMethod(arg, name, descriptor, signature, exceptions);
        }

        MethodVisitor mv = super.visitMethod(arg, name, descriptor, signature, exceptions);
        return new ApmMethodAdapter(mv, fileName, className, name);
    }
}
