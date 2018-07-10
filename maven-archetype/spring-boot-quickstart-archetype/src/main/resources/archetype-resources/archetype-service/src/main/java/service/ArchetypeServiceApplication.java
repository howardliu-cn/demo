#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <br>created at 18-6-19
 *
 * @author liuxh
 * @since 1.1.0
 */
@SpringBootApplication
public class ArchetypeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArchetypeServiceApplication.class, args);
    }
}
