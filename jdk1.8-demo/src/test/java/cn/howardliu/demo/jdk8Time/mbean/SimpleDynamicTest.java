package cn.howardliu.demo.jdk8Time.mbean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-5-9
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class SimpleDynamicTest {
    @Before
    public void setUp() throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("cn.howardliu.demo:type=SimpleDynamic");
        mBeanServer.registerMBean(new SimpleDynamic(), objectName);
    }

    @After
    public void tearDown() throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("cn.howardliu.demo:type=SimpleDynamic,State=test");
        mBeanServer.unregisterMBean(objectName);
    }

    @Test
    public void test() throws Exception {
//        MBeanServer mBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
//        Set<ObjectName> objectNames = mBeanServer.queryNames(new ObjectName("*:type=SimpleDynamic,*"), null);
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = mBeanServer
                .queryNames(new ObjectName("*:type=SimpleDynamic,*"), null);
        for (ObjectName objectName : objectNames) {
            System.out.println("State: " + mBeanServer.getAttribute(objectName, "State").toString());
            mBeanServer.setAttribute(objectName, new Attribute("State", "test"));
            System.out.println("State: " + mBeanServer.getAttribute(objectName, "State").toString());
            System.out.println("nbChanges: " + mBeanServer.getAttribute(objectName, "NbChanges").toString());
            mBeanServer.invoke(objectName, "reset", new Object[0], new String[0]);
            System.out.println("State: " + mBeanServer.getAttribute(objectName, "State").toString());
            System.out.println("nbChanges: " + mBeanServer.getAttribute(objectName, "NbChanges").toString());
        }
    }
}