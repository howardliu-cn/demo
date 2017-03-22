package cn.howardliu.demo.apm;

import java.lang.instrument.Instrumentation;

/**
 * <br>created at 17-3-20
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Agent {
    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new ApmClassFileTransformer());
    }

    public static void main(String[] args) {
        System.out.println(getVersionInfo());
    }

    public static String getVersionInfo() {
        Package pkg = Agent.class.getPackage();
        if (pkg == null) {
            return "my-agent";
        } else {
            return pkg.getImplementationTitle() + " : " + pkg.getImplementationVersion();
        }
    }
}
