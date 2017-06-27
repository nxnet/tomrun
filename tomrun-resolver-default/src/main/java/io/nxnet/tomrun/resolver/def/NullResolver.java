package io.nxnet.tomrun.resolver.def;

import io.nxnet.tomrun.resolver.ValueResolver;
import io.nxnet.tomrun.resolver.ValueResolverException;

public class NullResolver implements ValueResolver
{
    @Override
    public String resolve() throws ValueResolverException
    {
        return null;
    }
}
