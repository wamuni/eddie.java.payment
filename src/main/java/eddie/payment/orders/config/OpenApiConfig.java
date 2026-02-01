package eddie.payment.orders.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI ordersOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Orders Service API")
				.version("v1")
				.description("Customer, Product, Orders and Payments APIs (JDBC + Flyway)."));
	}
}
