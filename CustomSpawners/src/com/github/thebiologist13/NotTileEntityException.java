package com.github.thebiologist13;

/**
 * This is an exception for when a block is not a tile entity.
 * 
 * @author thebiologist13
 */
public class NotTileEntityException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public NotTileEntityException() {
		super();
	}
	
	public NotTileEntityException(String message) {
		super(message);
	}
	
	public NotTileEntityException(Throwable cause) {
		super(cause);
	}
	
	public NotTileEntityException(String message, Throwable cause) {
		super(message, cause);
	}

}
