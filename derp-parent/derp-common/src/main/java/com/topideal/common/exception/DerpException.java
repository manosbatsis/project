package com.topideal.common.exception;

public class DerpException extends RuntimeException{

	private static final long serialVersionUID = -2753217262643429954L;
	
	private Object obj ;
	
	public DerpException() {
		super() ;
	}
	
	public DerpException(String message){
		super(message) ;
	}
	
	public DerpException(String message, Object obj){
		super(message) ;
		
		this.obj = obj ;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
