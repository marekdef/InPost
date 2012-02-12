package net.retsat1.starlab.inpost.exceptions;

public class HttpRequestException extends Exception{

	public HttpRequestException(String reasonPhrase) {
		super(reasonPhrase);
	}
	
	public HttpRequestException(String reasonPhrase, Throwable t) {
		super(reasonPhrase, t);
	}
	
	public HttpRequestException( Throwable t) {
		super(t);
	}

}
