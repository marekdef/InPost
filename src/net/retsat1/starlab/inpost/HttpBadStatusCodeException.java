package net.retsat1.starlab.inpost;

public class HttpBadStatusCodeException extends HttpRequestException {

	public HttpBadStatusCodeException(String reasonPhrase) {
		super(reasonPhrase);
	}
	
	public HttpBadStatusCodeException(String reasonPhrase, Throwable t) {
		super(reasonPhrase, t);
	}

}
