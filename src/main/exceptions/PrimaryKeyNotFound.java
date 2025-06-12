package main.exceptions;

public class PrimaryKeyNotFound extends Exception {
    private static final long serialVersionUID = -1389494676398525746L;
    public PrimaryKeyNotFound(String msg) {
        this(msg, null);
    }
    public PrimaryKeyNotFound(String msg, Throwable e) {
        super(msg, e);
    }
}
