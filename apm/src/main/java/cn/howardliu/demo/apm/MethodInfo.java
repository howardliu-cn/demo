package cn.howardliu.demo.apm;

/**
 * <br>created at 17-3-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MethodInfo {
    private String className;
    private String methodName;
    private String fileName;
    private int lineNum;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
}
