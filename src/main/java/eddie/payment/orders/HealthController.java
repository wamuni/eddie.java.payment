package eddie.payment.orders;

import java.util.Map;

import eddie.payment.orders.aop.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
	@GetMapping("/health")
	public Map<String, String> health() {
		return Map.of("status", "ok");
	}

	@Timed
	@GetMapping("/aop-test")
	public String test() { return "ok"; }
}
