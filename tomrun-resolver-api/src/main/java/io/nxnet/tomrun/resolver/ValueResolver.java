package io.nxnet.tomrun.resolver;

public interface ValueResolver
{
    String resolve() throws ValueResolverException;
}
