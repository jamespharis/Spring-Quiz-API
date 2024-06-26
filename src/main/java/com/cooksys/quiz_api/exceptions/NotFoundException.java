package com.cooksys.quiz_api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7412603430738119816L;
	
	private String message;

}
