package gg.bayes.challenge.exception;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import gg.bayes.challenge.exception.dto.ErrorResponse;

/**
 * Makes sure that error handling is done according to API guidelines {@link ExceptionControllerAdvice}.
 * Format of the error response - {@link ErrorResponse}.
 *
 * @author Bharadwaj Adepu
 * @see ExceptionControllerAdvice
 * @see ErrorResponse
 * @since 0.0.1.RELEASE
 */
public class ExceptionControllerAdviceTest {


    private ExceptionControllerAdvice advice;

    private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void setUp() throws Exception {
        this.advice = new ExceptionControllerAdvice();
        this.httpServletRequest = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    public void testHandleResourceNotFoundException() throws Exception {
    	Mockito.when(this.httpServletRequest.getRequestURI()).thenReturn("/api/match/1/abyssal/items");


        ErrorResponse errorResponse = this.advice.handleResourceNotFoundException(
                new ResourceNotFoundException("Items", "1 , abyssal"), this.httpServletRequest).getBody();
        assertThat(new Integer(404)).isEqualTo(errorResponse.getStatus());
        assertThat("/api/match/1/abyssal/items").isEqualTo(errorResponse.getInstance());
        assertThat("Resource not found").isEqualTo(errorResponse.getTitle());
        assertThat("Resource 1 , abyssal of type Items cannot be found").isEqualTo(errorResponse.getDetail());

        Mockito.verify(this.httpServletRequest, Mockito.times(1)).getRequestURI();
        Mockito.verifyNoMoreInteractions(this.httpServletRequest);
    }
    
    @Test
    public void testHandleException() throws Exception {
    	Mockito.when(this.httpServletRequest.getRequestURI()).thenReturn("/api/match/1/abyssal/items");

    	ErrorResponse errorResponse = this.advice.handleException(
    			new RuntimeException("An unexpected error has occurred"), this.httpServletRequest).getBody();
    	assertThat(new Integer(500)).isEqualTo(errorResponse.getStatus());
    	assertThat("/api/match/1/abyssal/items").isEqualTo(errorResponse.getInstance());
    	assertThat("Internal Error").isEqualTo(errorResponse.getTitle());
    	assertThat("An unexpected error has occurred").isEqualTo(errorResponse.getDetail());

        Mockito.verify(this.httpServletRequest, Mockito.times(1)).getRequestURI();
        Mockito.verifyNoMoreInteractions(this.httpServletRequest);
    }
}
