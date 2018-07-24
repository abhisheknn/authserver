package com.micro.auth.exception;

import java.util.Date;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class MicroResponseEntityExceptionHandler 
extends ResponseEntityExceptionHandler {
 //Logger logger= Logger.getLogger(MicroResponseEntityExceptionHandler.class);
	 private static final Logger logger = LogManager.getLogger(MicroResponseEntityExceptionHandler.class);
	//@Override
	@ExceptionHandler(UserNotFoundException.class)
	protected ResponseEntity<Object> handleUserNotFoundExceptionInternal(UserNotFoundException ex, WebRequest request) {
		ExceptionResponse exceptionResponse=	new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		logger.info("user not found");
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse=	new ExceptionResponse(new Date(), "Validation Failed", ex.getBindingResult().toString());
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
}
