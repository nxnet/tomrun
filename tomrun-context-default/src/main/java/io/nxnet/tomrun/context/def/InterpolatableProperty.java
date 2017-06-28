package io.nxnet.tomrun.context.def;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.nxnet.tomrun.context.OwnableContext;
import io.nxnet.tomrun.interpolation.InterpolationStrategy;
import io.nxnet.tomrun.interpolation.Interpolator;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;
import io.nxnet.tomrun.property.RunContext;
import io.nxnet.tomrun.resolver.ResolveStrategy;
import io.nxnet.tomrun.scopes.CacheException;
import io.nxnet.tomrun.scopes.CachedValue;
import io.nxnet.tomrun.scopes.InProcessCachedValue;
import io.nxnet.tomrun.scopes.ValueInitializationException;
import io.nxnet.tomrun.scopes.ValueInitializationStrategy;

public class InterpolatableProperty implements Property
{
    protected final Property property;

    protected final OwnableContext propertyContext;

    protected final Interpolator propertyInterpolator;

    protected final Interpolator attributeInterpolator;

    protected final CachedValue<RunContext, String> interpolatedValueCache;

    protected InterpolationStrategy interpolationStrategy;

    protected InterpolationStrategy defaultInterpolationStrategy;

    public InterpolatableProperty(Property property, OwnableContext propertyContext)
    {
        this.property = property;
        this.propertyContext = propertyContext;
        this.propertyInterpolator = new Interpolator(Interpolator.PROPERTIES_INTERPOLATION_PATTERN);
        this.attributeInterpolator = new Interpolator(Interpolator.ATTRIBUTES_INTERPOLATION_PATTERN);
        this.interpolatedValueCache = new InProcessCachedValue<RunContext, String>();
        this.interpolationStrategy = property.getInterpolationStrategy();
        this.defaultInterpolationStrategy = InterpolationStrategy.NEVER;
    }

    @Override
    public String getName()
    {
        return this.property.getName();
    }

    @Override
    public String getValue(final RunContext runContext) throws PropertyException
    {
        // Resolve found property value
        final String propertyValue = this.property.getValue(runContext);

        // Calculate interpolation strategy if it was not explicitly specified
        if (this.interpolationStrategy == null)
        {
            this.calculateInterpolationStrategy(propertyValue, runContext);
        }

        // Interpolate properties or attributes
        String interpolatedValue = null;
        RunContext cacheId = null;
        switch (interpolationStrategy)
        {
            case NEVER:
                interpolatedValue = propertyValue;
                break;
            case ALWAYS:
                interpolatedValue = this.attributeInterpolator.interpolate(
                        this.propertyInterpolator.interpolate(propertyValue,
                                new PropertyReplacementStrategy(this.property.getName(), this.propertyContext)),
                        new AttributeReplacementStrategy(this.propertyContext));
                break;
            case ONCE:
                if (cacheId == null)
                {
                    cacheId = RunContext.builder().build();
                }
            case PER_HOST:
                if (cacheId == null)
                {
                    cacheId = RunContext.builder().withHostName(runContext.getHostName()).build();
                }
            case PER_PROCES:
                if (cacheId == null)
                {
                    cacheId = RunContext.builder().withHostName(runContext.getHostName()).withPid(runContext.getPid())
                        .build();
                }
            case PER_THREAD:
                if (cacheId == null)
                {
                    cacheId = RunContext.builder().withHostName(runContext.getHostName()).withPid(runContext.getPid())
                        .withThreadName(runContext.getThreadName()).build();
                }
            case PER_RUN:
                if (cacheId == null)
                {
                    cacheId = RunContext.builder().withHostName(runContext.getHostName()).withPid(runContext.getPid())
                        .withThreadName(runContext.getThreadName()).withRunNumber(runContext.getRunNumber()).build();
                }
            default:
                try
                {
                    interpolatedValue = this.interpolatedValueCache.getValue(cacheId, 
                            new ValueInitializationStrategy<String>() {

                        @Override
                        public String initializeValue() throws ValueInitializationException
                        {
                            return attributeInterpolator.interpolate(
                                    propertyInterpolator.interpolate(propertyValue,
                                            new PropertyReplacementStrategy(property.getName(), propertyContext)),
                                    new AttributeReplacementStrategy(propertyContext));
                        }
                    });
                }
                catch (CacheException e)
                {
                    throw new PropertyException(e);
                }
                break;
        }

        return interpolatedValue;
    }

    @Override
    public InterpolationStrategy getInterpolationStrategy()
    {
        return this.interpolationStrategy;
    }

    @Override
    public ResolveStrategy getResolveStrategy()
    {
        return this.property.getResolveStrategy();
    }

    /**
     * Calculate interpolation strategy based on property value and properties and attributes referred in it.
     *  
     * @param propertyValue
     * @param runContext
     * @throws PropertyException
     */
    synchronized void calculateInterpolationStrategy(String propertyValue, RunContext runContext) throws PropertyException
    {
        // Property value refers to some attribute
        if (this.interpolateAttribute(propertyValue)) 
        {
            this.interpolationStrategy = InterpolationStrategy.ALWAYS;
        }
        // Property value refers to another property
        else if (this.interpolateProperty(propertyValue))
        {
            // Get referred properties interpolation strategies 
            List<InterpolationStrategy> refPropsInterpolationStrategies = new ArrayList<InterpolationStrategy>();
            List<String> referredProperties = this.propertyInterpolator.interpolationTokens(propertyValue);
            Property referredPropertyObject = null;
            for (String referredProperty : referredProperties)
            {
                // If this property refers to a property with the same name, i.e.
                // property is referencing equally named property from higher context
                if (referredProperty != null && referredProperty.equals(this.getName()))
                {
                    referredPropertyObject = this.propertyContext.findPropertyObject(referredProperty, true);
                }
                // Else, property is referencing NOT equally named property from its context or higher context
                else
                {
                    referredPropertyObject = this.propertyContext.findPropertyObject(referredProperty);
                }

                // We found referred property
                if (referredPropertyObject != null)
                {
                    if (referredPropertyObject.getInterpolationStrategy() == null)
                    {
                        // Trigger referred property interpolation strategy smart detection
                        referredPropertyObject.getValue(runContext);
                    }
                    refPropsInterpolationStrategies.add(referredPropertyObject.getInterpolationStrategy());
                }
            }

            // Choose most suitable interpolation strategy
            this.interpolationStrategy = this.chooseMostAppropriateInterpolationStrategy(
                    refPropsInterpolationStrategies, this.defaultInterpolationStrategy);
        }
        // Property value doesn't refer neither to some attribute nor to another property  
        else
        {
            this.interpolationStrategy = this.defaultInterpolationStrategy;
        }
    }

    boolean interpolateAttribute(String propertyValue)
    {
        if (propertyValue != null && propertyValue.contains("#{")) 
        {
            return true;
        }
        return false;
    }


    boolean interpolateProperty(String propertyValue)
    {
        if (propertyValue != null && propertyValue.contains("${")) 
        {
            return true;
        }
        return false;
    }

    InterpolationStrategy chooseMostAppropriateInterpolationStrategy(
            List<InterpolationStrategy> parentPropertiesInterpolationStrategies, InterpolationStrategy defaultStrategy)
    {
        if (parentPropertiesInterpolationStrategies != null && !parentPropertiesInterpolationStrategies.isEmpty())
        {
            Collections.sort(parentPropertiesInterpolationStrategies);
            InterpolationStrategy mostScopeRestrictedStrategy = parentPropertiesInterpolationStrategies.get(0);
            if (InterpolationStrategy.NEVER.equals(mostScopeRestrictedStrategy))
            {
                mostScopeRestrictedStrategy = InterpolationStrategy.ONCE;
            }
            return mostScopeRestrictedStrategy;
        }
        return defaultStrategy;
    }

    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return attributeInterpolator.hashCode();
    }

    /**
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        return attributeInterpolator.equals(obj);
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
        if (interpolationStrategy != null)
        {
            builder.append("interpolationStrategy: ").append(interpolationStrategy);
        }
        builder.append("\n}");
        return builder.toString();
    }

}
