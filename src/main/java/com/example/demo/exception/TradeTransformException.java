package com.example.demo.exception;

public class TradeTransformException extends TradeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TradeTransformException(){
		super();
	}
	
	public TradeTransformException(String message){
		super(message);
	}
	
	public TradeTransformException(Throwable cause){
		super(cause);
	}
	
	public TradeTransformException(String message, Throwable cause){
		super(message, cause);
	}
}
