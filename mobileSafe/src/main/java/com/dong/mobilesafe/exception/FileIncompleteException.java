package com.dong.mobilesafe.exception;

/**
 * 文件不完整异常
 *
 */
public class FileIncompleteException extends RuntimeException {

	private static final long serialVersionUID = 3903510915492879317L;

	public FileIncompleteException() {
    	super();
    }
    
    public FileIncompleteException(String detailMessage) {
        super(detailMessage);
    }
	
}
