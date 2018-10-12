package org.ulitzky.hotels.api.exception;

public class NegativeInputParameterException extends Exception {
	
	public NegativeInputParameterException(final String paramName, final int paramValue) 
	{
		super(String.format("Invalid input parameter %s - non-negative value excepted, received %d", paramName, paramValue));
	}

}
