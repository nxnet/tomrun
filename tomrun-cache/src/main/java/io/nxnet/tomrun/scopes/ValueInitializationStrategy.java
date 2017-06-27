package io.nxnet.tomrun.scopes;

public interface ValueInitializationStrategy<T>
{
    T initializeValue() throws ValueInitializationException;
}
