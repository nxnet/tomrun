package io.nxnet.tomrun.alias;

public class AliasException extends Exception {

    public AliasException() {
    }

    public AliasException(String message) {
        super(message);
    }

    public AliasException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliasException(Throwable cause) {
        super(cause);
    }
}
