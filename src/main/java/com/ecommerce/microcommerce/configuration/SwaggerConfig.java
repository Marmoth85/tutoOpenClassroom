package com.ecommerce.microcommerce.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:swagger/product.properties")
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi api() {
        /*return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ecommerce.microcommerce.web.controller")
                        //.or(RequestHandlerSelectors.basePackage("autre.package.pour.tester.plusieurs.predicats"))
                )
                .paths(PathSelectors.regex("/Produits.*")
                        //.or(PathSelectors.regex("/test.*"))
                )
                .build()
                .tags(new Tag(PRODUCT_TAG, "API pour la gestion des produits"));
         */
        return GroupedOpenApi.builder()
                .group("Product API")
                .pathsToMatch("/Produits/**", "/AdminProduits/**", "/TriProduits/**")
                .build();
    }

    @Bean
    public OpenAPI productAPI() {
        return new OpenAPI().info(new Info()
                .title("MicroCommerce")
                .description("Tuto OpenClassroom sur les API REST et les micro-services")
                .version("0.0.1"));
    }
}
