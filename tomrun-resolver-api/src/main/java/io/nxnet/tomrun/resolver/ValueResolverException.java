package io.nxnet.tomrun.resolver;

public class ValueResolverException extends Exception {

    public ValueResolverException() {
    }

    public ValueResolverException(String message) {
        super(message);
    }

    public ValueResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueResolverException(Throwable cause) {
        super(cause);
    }
}
