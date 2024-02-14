package com.didan.social;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "API DOCUMENTS FOR FORUM APP",
				version = "1.0.0",
				description = "This api documents decribe how to use our forum application",
				termsOfService = "Zdidane",
				license = @License(
						name = "Nguyen Di Dan",
						url = "https://znnguyenportfolio.netlify.app/"
				)
		)
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
public class SocialApplication {
	private static Logger logger = LoggerFactory.getLogger(SocialApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SocialApplication.class, args);
		logger.info("Server is running");
	}

}
