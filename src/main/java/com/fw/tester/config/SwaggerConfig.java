package com.fw.tester.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.fw.tester.controller";
	
	@Value(value = "${swagger.enabled}")
	Boolean swaggerEnabled;
	
	@Bean
	public Docket createRestApi() {		
		return new Docket(DocumentationType.SWAGGER_2)		
				.apiInfo(apiInfo())				
				.enable(swaggerEnabled)
				.select()				
				.apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
	}
	
	private ApiInfo apiInfo(){
		return new ApiInfoBuilder()
				.title("FW Tester Integration")
				.description("FW Tester Integration Api")				
				.version("1.0.0")
				.build();
	}
	
}
