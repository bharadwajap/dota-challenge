package gg.bayes.challenge.exception;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import gg.bayes.challenge.exception.dto.ErrorResponse;


/**
 * Allows to handle all expected and unexpected errors occurred while processing the request.
 * 
 * @author Bharadwaj.Adepu
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

	private static final String APPLICATION_PROBLEM_JSON = "application/problem+json";

    /**
     * Handles a case when requested resource cannot be found
     *
     * @param e any exception of type {@link Exception}
     * @return {@link ResponseEntity} containing standard body in case of errors
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public HttpEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e,
                                                                     final HttpServletRequest request) {
    	String message = String.format("Resource %s of type %s cannot be found", e.getResourceId(), e.getResourceType());
        LOGGER.error(message);

        ErrorResponse problem = new ErrorResponse("Resource not found", message);
        problem.setStatus(HttpStatus.NOT_FOUND.value());
        problem.setInstance(request.getRequestURI());

        return new ResponseEntity<>(problem, overrideContentType(), HttpStatus.NOT_FOUND);
    }
    
	/**
	 * Handles all unexpected situations
	 *
	 * @param e any exception of type {@link Exception}
	 * @return {@link ResponseEntity} containing standard body in case of errors
	 */
	@ExceptionHandler(Exception.class)
	public HttpEntity<ErrorResponse> handleException(Exception e,
			final HttpServletRequest request) {

		LOGGER.error("An unexpected error has occurred", e);

		ErrorResponse problem = new ErrorResponse("Internal Error", e.getMessage());
		problem.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		problem.setInstance(request.getRequestURI());

		return new ResponseEntity<>(problem, overrideContentType(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private HttpHeaders overrideContentType() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Content-Type", APPLICATION_PROBLEM_JSON);
		return httpHeaders;
	}

}