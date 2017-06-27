package io.nxnet.tomrun.property.def.util;

import io.nxnet.tomrun.interpolation.InterpolationStrategy;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;
import io.nxnet.tomrun.property.RunContext;
import io.nxnet.tomrun.resolver.ResolveStrategy;

public class FixedNumberDecorator implements Property
{
    private Property delegee;

    public FixedNumberDecorator(Property delegee)
    {
        this.delegee = delegee;
    }

    @Override
    public String getName()
    {
        return this.delegee.getName();
    }

    @Override
    public String getValue(RunContext runContext) throws PropertyException
    {
        return this.delegee.getValue(runContext) + "000";
    }

    @Override
    public ResolveStrategy getResolveStrategy()
    {
        return ResolveStrategy.ALWAYS;
    }

    @Override
    public InterpolationStrategy getInterpolationStrategy()
    {
        return this.delegee.getInterpolationStrategy();
    }

}
