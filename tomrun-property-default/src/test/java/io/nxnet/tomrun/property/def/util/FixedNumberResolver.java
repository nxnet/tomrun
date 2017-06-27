package io.nxnet.tomrun.property.def.util;

import java.util.concurrent.atomic.AtomicInteger;

import io.nxnet.tomrun.resolver.ValueResolver;
import io.nxnet.tomrun.resolver.ValueResolverException;

public class FixedNumberResolver implements ValueResolver
{
    public static final AtomicInteger VALUE_COUNTER = new AtomicInteger(0);

    private String valueTemplate;

    public FixedNumberResolver() 
    {
        this("100");
    }

    public FixedNumberResolver(String value) 
    {
        this.valueTemplate = value;
    }

    @Override
    public String resolve() throws ValueResolverException
    {
        String val = new Integer(this.valueTemplate).toString();
        VALUE_COUNTER.incrementAndGet();
        return val;
    }
}
