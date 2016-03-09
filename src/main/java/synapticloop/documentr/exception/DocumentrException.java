package synapticloop.documentr.exception;

public class DocumentrException extends Exception {
	private static final long serialVersionUID = 3499859602264427962L;

	public DocumentrException(String message) {
		super(message);
	}

	public DocumentrException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
