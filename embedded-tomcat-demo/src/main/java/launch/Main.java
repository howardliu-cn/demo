package launch;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.ServletException;
import java.io.File;

/**
 * <br>created at 17-5-9
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class Main {
    public static void main(String[] args) throws ServletException, LifecycleException {
        String webappDirLocation = "embedded-tomcat-demo/src/main/webapp/";
        Tomcat tomcat = new Tomcat();

        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        tomcat.setPort(Integer.valueOf(webPort));

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());

        File additionWebInfClasses = new File("embedded-tomcat-demo/target/classes");
        WebResourceRoot resources = new StandardRoot();
        resources.addPreResources(
                new DirResourceSet(
                        resources,
                        "/WEB-INF/classes",
                        additionWebInfClasses.getAbsolutePath(),
                        "/")
        );
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}
