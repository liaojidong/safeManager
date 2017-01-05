package com.dong.mobilesafe.exception;

public class NetworkDataException extends RuntimeException{

	private static final long serialVersionUID = -28195997053558068L;

	public NetworkDataException() {
    	super();
    }
    
    public NetworkDataException(String detailMessage) {
        super(detailMessage);
    }

}
