package eddie.payment.orders.common;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import eddie.payment.orders.order.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class ApiExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ErrorResponse handleDuplicateKey(DataIntegrityViolationException ex) {
		log.warn("Conflict: {}", safeMessage(ex));
		return new ErrorResponse("duplicate_resource", "Email already exists");
	}

	private String safeMessage(DataIntegrityViolationException ex) {
		Throwable cause = ex.getMostSpecificCause();
		return (cause != null) ? cause.getMessage() : ex.getMessage();
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({
		OrderService.OrderNotFoundException.class,
		OrderService.CustomerMissingException.class,
		OrderService.ProductMissingException.class
	})
	public ErrorResponse handleNotFound(RuntimeException ex) {
		log.warn("404 NOT FOUND: {}", ex);
		return new ErrorResponse("not_found", ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResponse handleBadRequest(IllegalArgumentException ex) {
		log.warn("502 BAD REQUEST: {}", ex);
		return new ErrorResponse("bad_request", ex.getMessage());
	}

	record ErrorResponse(String code, String message) {};
}
