package io.nxnet.tomrun.property.def;

import io.nxnet.tomrun.interpolation.InterpolationStrategy;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;
import io.nxnet.tomrun.property.RunContext;
import io.nxnet.tomrun.resolver.ResolveStrategy;
import io.nxnet.tomrun.resolver.ValueResolver;
import io.nxnet.tomrun.resolver.ValueResolverException;

public class DefaultProperty implements Property
{
    protected String name;

    protected ValueResolver valueResolver;

    protected ResolveStrategy resolveStrategy;

    protected InterpolationStrategy interpolationStrategy;

    public DefaultProperty(String name, ValueResolver valueResolver)
    {
        this(name, valueResolver, InterpolationStrategy.NEVER);
    }

    public DefaultProperty(String name, ValueResolver valueResolver, InterpolationStrategy interpolationStrategy)
    {
        this.name = name;
        this.valueResolver = valueResolver;
        this.resolveStrategy = ResolveStrategy.ALWAYS;
        this.interpolationStrategy = interpolationStrategy;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getValue(RunContext runContext) throws PropertyException
    {
        try
        {
            return valueResolver.resolve();
        }
        catch (ValueResolverException e)
        {
            throw new PropertyException(e);
        }
    }

    @Override
    public ResolveStrategy getResolveStrategy()
    {
        return this.resolveStrategy;
    }

    @Override
    public InterpolationStrategy getInterpolationStrategy()
    {
        return this.interpolationStrategy;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DefaultProperty other = (DefaultProperty) obj;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n   class: ").append(getClass().getName()).append("\n   ");
        if (name != null)
        {
            builder.append("name: ").append(name).append("\n   ");
        }
        if (valueResolver != null)
        {
            builder.append("valueResolver: ").append(valueResolver.toString().replaceAll("(.*)", "   $1").trim()).append("\n   ");
        }
        if (resolveStrategy != null)
        {
            builder.append("resolveStrategy: ").append(resolveStrategy).append("\n   ");
        }
        if (interpolationStrategy != null)
        {
            builder.append("interpolationStrategy: ").append(interpolationStrategy);
        }
        builder.append("\n}");
        return builder.toString();
    }

}
