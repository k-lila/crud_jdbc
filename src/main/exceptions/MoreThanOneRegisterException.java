package main.exceptions;

public class MoreThanOneRegisterException extends Exception {
	private static final long serialVersionUID = -7509649433607067138L;
    public MoreThanOneRegisterException(String msg) {
        super(msg);
    }
}
