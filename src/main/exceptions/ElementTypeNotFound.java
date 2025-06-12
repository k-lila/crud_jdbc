package main.exceptions;

public class ElementTypeNotFound extends Exception {
	private static final long serialVersionUID = -2268140970978666251L;

	public ElementTypeNotFound(String msg) {
        this(msg, null);
    }

    public ElementTypeNotFound(String msg, Throwable e) {
        super(msg, e);
    }
}
