package io.nxnet.tomrun.agent.ext;

import io.nxnet.tomrun.resolver.ValueResolver;

public class LiteralResolver2 implements ValueResolver
{
    private String value;

    public LiteralResolver2(String value)
    {
        this.value = value;
    }

    @Override
    public String resolve()
    {
        return "LiteralResolver2_" + this.value;
    }

}
