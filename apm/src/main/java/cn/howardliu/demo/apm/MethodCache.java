package cn.howardliu.demo.apm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <br>created at 17-3-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MethodCache {
    private static List<MethodInfo> cacheMethods = Collections.synchronizedList(new ArrayList<MethodInfo>());

    public synchronized static MethodInfo get(int id) {
        return cacheMethods.get(id);
    }

    public synchronized static int request() {
        cacheMethods.add(new MethodInfo());
        return cacheMethods.size() - 1;
    }

    public synchronized static void updateLineNum(int id, int lineNum) {
        cacheMethods.get(id).setLineNum(lineNum);
    }

    public synchronized static void updateMethodName(int id, String fileName, String className, String methodName) {
        MethodInfo methodInfo = cacheMethods.get(id);
        methodInfo.setFileName(fileName);
        methodInfo.setClassName(className);
        methodInfo.setMethodName(methodName);
//        System.out.println("");
    }
}
