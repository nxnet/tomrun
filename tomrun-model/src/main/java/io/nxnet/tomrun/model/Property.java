package io.nxnet.tomrun.model;

import javax.annotation.Generated;

public class Property extends ResolvableElement
{
    private String name;

    private String filters;

    @Generated("SparkTools")
    private Property(Builder builder)
    {
        this.value = builder.value;
        this.valueResolver = builder.valueResolver;
        this.resolveStrategy = builder.resolveStrategy;
        this.interpolationStrategy = builder.interpolationStrategy;
        this.name = builder.name;
        this.filters = builder.filters;
    }

    public String getName()
    {
        return name;
    }

    public String getFilters()
    {
        return filters;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Property other = (Property) obj;
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
        builder.append("{\n   ").append("class: ").append(getClass().getName()).append("\n   ");
        if (name != null)
        {
            builder.append("name: ").append(name).append("\n   ");
        }
        if (filters != null)
        {
            builder.append("filters: ").append(filters).append("\n   ");
        }
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

    /**
     * Creates builder to build {@link Property}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link Property}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private String value;
        private String valueResolver;
        private String resolveStrategy;
        private String interpolationStrategy;
        private String name;
        private String filters;

        private Builder()
        {
        }

        public Builder withValue(String value)
        {
            this.value = value;
            return this;
        }

        public Builder withValueResolver(String valueResolver)
        {
            this.valueResolver = valueResolver;
            return this;
        }

        public Builder withResolveStrategy(String resolveStrategy)
        {
            this.resolveStrategy = resolveStrategy;
            return this;
        }

        public Builder withInterpolationStrategy(String interpolationStrategy)
        {
            this.interpolationStrategy = interpolationStrategy;
            return this;
        }

        public Builder withName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder withFilters(String filters)
        {
            this.filters = filters;
            return this;
        }

        public Property build()
        {
            return new Property(this);
        }
    }

}
