package io.nxnet.tomrun.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Generated;

public class Properties
{
    private String factory;

    private Set<Property> properties = new LinkedHashSet<Property>();

    @Generated("SparkTools")
    private Properties(Builder builder)
    {
        this.factory = builder.factory;
        this.properties = builder.properties;
    }

    public String getFactory()
    {
        return factory;
    }

    public void setFactory(String factory)
    {
        this.factory = factory;
    }

    public Set<Property> getProperties()
    {
        return properties;
    }

    public void setProperties(Set<Property> properties)
    {
        this.properties = properties;
    }

    public boolean addProperty(Property property)
    {
        if (properties == null)
        {
            properties = new LinkedHashSet<Property>();
        }

        return properties.add(property);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder2 = new StringBuilder();
        builder2.append("{\n   ").append("class: ").append(this.getClass().getName()).append("\n   ");
        if (factory != null)
        {
            builder2.append("factory: ").append(factory).append("\n   ");
        }
        if (properties != null)
        {
            builder2.append("properties: ").append(properties.toString().replaceAll("(.*)", "   $1").trim());
        }
        builder2.append("\n}");
        return builder2.toString();
    }

    /**
     * Creates builder to build {@link Properties}.
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to build {@link Properties}.
     */
    @Generated("SparkTools")
    public static final class Builder
    {
        private String factory;
        private Set<Property> properties;

        private Builder()
        {
        }

        public Builder withFactory(String factory)
        {
            this.factory = factory;
            return this;
        }

        public Builder withProperties(Set<Property> properties)
        {
            this.properties = properties;
            return this;
        }

        public Builder addProperty(Property property)
        {
            if (this.properties == null)
            {
                this.properties = new LinkedHashSet<Property>();
            }
            this.properties.add(property);
            return this;
        }

        public Properties build()
        {
            return new Properties(this);
        }
    }

}
