package cn.howardliu.demo.apm;

import java.util.HashSet;
import java.util.Set;

/**
 * <br>created at 17-3-22
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ApmFilter {
    private static Set<String> excludePackage = new HashSet<>();
    private static Set<String> excludeClassLoader = new HashSet<>();

    static {
        excludePackage.add("java/");
        excludePackage.add("sun/");
        excludePackage.add("com/sun/");
        excludePackage.add("org/apache/");
        excludePackage.add("org/springframework/");
        excludePackage.add("ch/qos/logback");
        excludePackage.add("org/hibernate");
        excludePackage.add("org/slf4j");
        excludePackage.add("org/jboss");
        excludePackage.add("com/fasterxml");
        excludePackage.add("cn/howardliu/apm");
    }

    public static void addExcludePackage(String p) {
        if (p == null || p.isEmpty()) {
            return;
        }
        excludePackage.add(p.replaceAll("\\.", "/"));
    }

    public static void addExcludeClass(String c) {
        if (c == null || c.isEmpty()) {
            return;
        }
        excludePackage.add(c.replaceAll("\\.", "/"));
    }

    public static void addExcludeClassLoader(String l) {
        if (l == null || l.isEmpty()) {
            return;
        }
        excludeClassLoader.add(l);
    }

    public static boolean isNotNeedInject(String className) {
        if (className == null || className.isEmpty()) {
            return true;
        }
        String _className = className.replaceAll("\\.", "/");
        for (String exclude : excludePackage) {
            if (_className.startsWith(exclude)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotNeedInjectClassLoader(String classLoader) {
        return classLoader == null || classLoader.isEmpty() || excludeClassLoader.contains(classLoader);
    }
}
