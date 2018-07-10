#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>created at 18-3-29
 *
 * @author liuxh
 * @since 1.1.0
 */
@RestController
public class IndexController {
    @RequestMapping("index")
    public String index() {
        return "Hello, World!";
    }
}
