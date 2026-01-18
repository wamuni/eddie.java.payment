package eddie.payment.orders.common;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;


@RestControllerAdvice
public class ApiExceptionHandler {
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ErrorResponse handleDuplicateKey(DataIntegrityViolationException ex) {
		return new ErrorResponse("duplicate_resource", "Email already exists");
	}

	record ErrorResponse(String code, String message) {};
}
