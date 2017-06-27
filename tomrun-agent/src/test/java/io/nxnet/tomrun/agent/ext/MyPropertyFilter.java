package io.nxnet.tomrun.agent.ext;

import io.nxnet.tomrun.interpolation.InterpolationStrategy;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;
import io.nxnet.tomrun.property.RunContext;
import io.nxnet.tomrun.resolver.ResolveStrategy;

public class MyPropertyFilter implements Property
{
    private Property property;

    public MyPropertyFilter(Property property)
    {
        this.property = property;
    }
    
    @Override
    public String getName()
    {
        return this.property.getName();
    }

    @Override
    public String getValue(RunContext runContext) throws PropertyException
    {
        return this.property.getValue(runContext).replace("valllllllll", "val");
    }

    @Override
    public InterpolationStrategy getInterpolationStrategy()
    {
        return this.property.getInterpolationStrategy();
    }

    @Override
    public ResolveStrategy getResolveStrategy()
    {
        return this.property.getResolveStrategy();
    }
}
