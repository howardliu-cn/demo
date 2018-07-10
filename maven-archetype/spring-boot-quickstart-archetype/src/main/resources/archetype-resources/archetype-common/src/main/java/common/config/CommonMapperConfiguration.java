#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * <br>created at 18-3-29
 *
 * @author liuxh
 * @since 0.1.0
 */
@Configuration
@MapperScan({
        "${package}.common.mapper"
})
public class CommonMapperConfiguration {
}
