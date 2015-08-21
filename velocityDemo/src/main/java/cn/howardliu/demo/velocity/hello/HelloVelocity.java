package cn.howardliu.demo.velocity.hello;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <br/>create at 15-8-21
 *
 * @author liuxh
 * @since 1.0.0
 */
public class HelloVelocity {
    public static void main(String[] args) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate("HelloVelocity.vm");
        VelocityContext ctx = new VelocityContext();
        ctx.put("name", "velocity");
        ctx.put("date", (new Date()).toString());
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        ctx.put("list", list);
        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);
        System.out.println(sw.toString());
    }
}
