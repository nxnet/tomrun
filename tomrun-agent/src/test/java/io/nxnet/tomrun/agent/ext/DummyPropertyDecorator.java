package io.nxnet.tomrun.agent.ext;

import io.nxnet.tomrun.interpolation.InterpolationStrategy;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;
import io.nxnet.tomrun.property.RunContext;
import io.nxnet.tomrun.resolver.ResolveStrategy;

public class DummyPropertyDecorator implements Property
{
    private Property property;

    public DummyPropertyDecorator(Property property)
    {
        this.property = property;
    }

    /**
     * @return
     * @see io.nxnet.tomrun.property.Property#getName()
     */
    public String getName()
    {
        return property.getName();
    }

    /**
     * @param runContext
     * @return
     * @throws PropertyException
     * @see io.nxnet.tomrun.property.Property#getValue(io.nxnet.tomrun.property.RunContext)
     */
    public String getValue(RunContext runContext) throws PropertyException
    {
        return property.getValue(runContext);
    }

    /**
     * @return
     * @see io.nxnet.tomrun.property.Property#getInterpolationStrategy()
     */
    public InterpolationStrategy getInterpolationStrategy()
    {
        return property.getInterpolationStrategy();
    }

    /**
     * @return
     * @see io.nxnet.tomrun.property.Property#getResolveStrategy()
     */
    public ResolveStrategy getResolveStrategy()
    {
        return property.getResolveStrategy();
    }

}
