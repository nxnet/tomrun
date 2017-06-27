package io.nxnet.tomrun.model;

public class ResolvableElement
{
    protected String value;

    protected String valueResolver;

    protected String resolveStrategy;

    protected String interpolationStrategy;

    public ResolvableElement()
    {
        this(null, null);
    }

    public ResolvableElement(String value)
    {
        this(value, null);
    }

    public ResolvableElement(String value, String valueResolver)
    {
        this.value = value;
        this.valueResolver = valueResolver;
    }

    public ResolvableElement(String value, String valueResolver, String resolveStrategy, String interpolationStrategy)
    {
        this.value = value;
        this.valueResolver = valueResolver;
        this.resolveStrategy = resolveStrategy;
        this.interpolationStrategy = interpolationStrategy;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValueResolver()
    {
        return valueResolver;
    }

    public void setValueResolver(String valueResolver)
    {
        this.valueResolver = valueResolver;
    }

    /**
     * @return the resolveStrategy
     */
    public String getResolveStrategy()
    {
        return resolveStrategy;
    }

    /**
     * @param resolveStrategy the resolveStrategy to set
     */
    public void setResolveStrategy(String resolveStrategy)
    {
        this.resolveStrategy = resolveStrategy;
    }

    /**
     * @return the interpolationStrategy
     */
    public String getInterpolationStrategy()
    {
        return interpolationStrategy;
    }

    /**
     * @param interpolationStrategy the interpolationStrategy to set
     */
    public void setInterpolationStrategy(String interpolationStrategy)
    {
        this.interpolationStrategy = interpolationStrategy;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ResolvableElement {\n   ");
        if (value != null)
        {
            builder.append("value: ").append(value).append("\n   ");
        }
        if (valueResolver != null)
        {
            builder.append("valueResolver: ").append(valueResolver).append("\n   ");
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
