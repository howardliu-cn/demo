package cn.howardliu.javassistDemo;

import cn.howardliu.javassistDemo.clazz.Run;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Method;

/**
 * <br>created at 17-4-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        ClassPool pool = ClassPool.getDefault();
        CtClass clazz;
        try {
            clazz = pool.get("cn.howardliu.javassistDemo.clazz.Run");
        } catch (NotFoundException e) {
            clazz = pool.makeClass("cn.howardliu.javassistDemo.clazz.Run");
        }
        if (clazz == null) {
            System.out.println("null");
            return;
        }
        try {
            CtClass[] interfaces = clazz.getInterfaces();
            for (CtClass anInterface : interfaces) {
                System.out.println(anInterface.getName());
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            CtClass superclass = clazz.getSuperclass();
            if (superclass == null) {
                return;
            }
            System.out.println(superclass.getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        Class a = null;
        try {
            a = clazz.toClass();
        } catch (CannotCompileException ignored) {
        }

        if(a == null) {
            return;
        }

        try {
            ProxyFactory factory = new ProxyFactory();
            factory.setSuperclass(a);
            factory.setFilter(m -> "action".equals(m.getName()));
            Class aClass = factory.createClass();
            Object o = aClass.newInstance();
            if (o instanceof ProxyObject) {
                ((ProxyObject) o).setHandler((self, thisMethod, proceed, args1) -> {
                    long time = -System.currentTimeMillis();
                    System.out.println("begin");
                    Object result = proceed.invoke(self, args1);
                    System.out.println("end");
                    time += System.currentTimeMillis();
                    System.out.println("used " + time + "ms");
                    return result;
                });
            }

            if(o instanceof Run) {
                ((Run) o).action();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
