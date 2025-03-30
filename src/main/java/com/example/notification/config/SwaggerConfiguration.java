package com.example.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition
public class SwaggerConfiguration {

	@Bean
	public OpenAPI apiDetails() {

		return new OpenAPI()
				.info(new Info().title("Real Time Notification").version("1.0")
						.contact(new Contact().name("Akash Verma").email("averma.akash@gmail.com")))
				.addSecurityItem(new SecurityRequirement().addList("jwtToken"))
				.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("jwtToken",
						new SecurityScheme().name("jwtToken") // Use the same cookie name
								.type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.COOKIE))); // Swagger will use
																									// this cookie;

	}

}
