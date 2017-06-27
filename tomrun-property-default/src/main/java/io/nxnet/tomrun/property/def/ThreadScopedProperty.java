package io.nxnet.tomrun.property.def;

import io.nxnet.tomrun.interpolation.InterpolationStrategy;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;
import io.nxnet.tomrun.property.RunContext;
import io.nxnet.tomrun.resolver.ResolveStrategy;
import io.nxnet.tomrun.scopes.CacheException;
import io.nxnet.tomrun.scopes.CachedValue;
import io.nxnet.tomrun.scopes.InProcessCachedValue;
import io.nxnet.tomrun.scopes.ValueInitializationException;
import io.nxnet.tomrun.scopes.ValueInitializationStrategy;

public class ThreadScopedProperty implements Property
{
    protected final Property property;

    protected final ResolveStrategy resolveStrategy;

    protected final CachedValue<RunContext, String> threadValues;

    public ThreadScopedProperty(Property property)
    {
        this.property = property;
        this.resolveStrategy = ResolveStrategy.PER_THREAD;
        this.threadValues = new InProcessCachedValue<RunContext, String>();
    }

    public String getName()
    {
        return property.getName();
    }

    @Override
    public String getValue(final RunContext runContext) throws PropertyException
    {
        // Validate args
        if (runContext == null)
        {
            throw new IllegalArgumentException("run context argument is null");
        }

        // Make thread scoped cache key
        final RunContext threadRunContext = RunContext.builder()
                .withHostName(runContext.getHostName())
                .withPid(runContext.getPid())
                .withThreadName(runContext.getThreadName())
                .withRunNumber(0)
                .build();

        try
        {
            return this.threadValues.getValue(threadRunContext, new ValueInitializationStrategy<String>() {

                @Override
                public String initializeValue() throws ValueInitializationException
                {
                    try
                    {
                        return property.getValue(runContext);
                    }
                    catch (PropertyException e)
                    {
                        throw new ValueInitializationException(e);
                    }
                }
            });
        }
        catch (CacheException e)
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
        return this.property.getInterpolationStrategy();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.property.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        return this.property.equals(obj);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n   class: ").append(getClass().getName()).append("\n   ");
        if (property != null)
        {
            builder.append("property: ").append(property.toString().replaceAll("(.*)", "   $1").trim()).append("\n   ");
        }
        if (resolveStrategy != null)
        {
            builder.append("resolveStrategy: ").append(resolveStrategy);
        }
        builder.append("\n}");
        return builder.toString();
    }

}
