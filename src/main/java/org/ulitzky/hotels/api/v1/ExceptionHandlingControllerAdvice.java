package org.ulitzky.hotels.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.ulitzky.hotels.api.exception.NegativeInputParameterException;

@ControllerAdvice
public class ExceptionHandlingControllerAdvice extends ResponseEntityExceptionHandler {

	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ExceptionHandler(NegativeInputParameterException.class)
	  public void handleBadRequest() {
	        // Nothing to do
	    }
}
