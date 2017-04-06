package cn.howardliu.demo.apm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <br>created at 17-3-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ApmCounter {
    public static AtomicInteger classCount = new AtomicInteger(0);
    public static AtomicInteger methodCount = new AtomicInteger(0);

    private static final Map<Integer, Long> timeCounter = new ConcurrentHashMap<>();

    public static void start(int methodId) {
        timeCounter.put(methodId, System.nanoTime());
    }

    public static void end(int methodId) {
        if (timeCounter.containsKey(methodId)) {
            MethodInfo methodInfo = MethodCache.get(methodId);
            long usedTime = System.nanoTime() - timeCounter.get(methodId);
//            System.out.println("method " + methodInfo.getClassName() + "#" + methodInfo.getMethodName()
//                    + " used " + usedTime + " ns");
        }
    }
}
