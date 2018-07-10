#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service.boot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;

/**
 * <br>created at 18-6-21
 *
 * @author liuxh
 * @since 0.1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        Contact contact = new Contact("Howard Liu", "http://www.howardliu.cn", "howardliu1988@163.com");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("${artifactId}")
                .description("Description for ${artifactId}")
                .version("1.0.0")
                .contact(contact)
                .build();
        HashSet<String> producesAndConsumes = new HashSet<>(Collections.singletonList("application/json"));
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("${package}"))
                .paths(PathSelectors.any())
                .build()
                .produces(producesAndConsumes)
                .consumes(producesAndConsumes)
                .globalResponseMessage(RequestMethod.GET, Collections.emptyList())
                .globalResponseMessage(RequestMethod.POST, Collections.emptyList())
                .globalOperationParameters(Collections.emptyList());
    }
}
