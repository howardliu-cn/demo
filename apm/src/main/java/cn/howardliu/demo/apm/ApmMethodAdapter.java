package cn.howardliu.demo.apm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

/**
 * <br>created at 17-3-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ApmMethodAdapter extends MethodVisitor {
    private int methodId = 0;

    public ApmMethodAdapter(MethodVisitor visitor, String fileName, String className, String methodName) {
        super(Opcodes.ASM5, visitor);
        methodId = MethodCache.request();
        MethodCache.updateMethodName(methodId, fileName, className, methodName);
        ApmCounter.methodCount.getAndIncrement();
    }

    @Override
    public void visitCode() {
        this.visitLdcInsn(methodId);
        this.visitMethodInsn(INVOKESTATIC, Type.getInternalName(ApmCounter.class), "start", "(I)V", false);
        super.visitCode();
    }

    @Override
    public void visitLineNumber(int line, Label label) {
        MethodCache.updateLineNum(methodId, line);
        super.visitLineNumber(line, label);
    }

    @Override
    public void visitInsn(int inst) {
        switch (inst) {
            case Opcodes.ARETURN:
            case Opcodes.DRETURN:
            case Opcodes.FRETURN:
            case Opcodes.IRETURN:
            case Opcodes.LRETURN:
            case Opcodes.RETURN:
            case Opcodes.ATHROW:
                this.visitLdcInsn(methodId);
                this.visitMethodInsn(INVOKESTATIC, Type.getInternalName(ApmCounter.class), "end", "(I)V", false);
            default:
                break;
        }
        super.visitInsn(inst);
    }
}
