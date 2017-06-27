package io.nxnet.tomrun.context.def;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;

import io.nxnet.tomrun.context.Context;
import io.nxnet.tomrun.context.Contextable;
import io.nxnet.tomrun.context.OwnableContext;
import io.nxnet.tomrun.executor.Executor;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;
import io.nxnet.tomrun.property.RunContext;

public class DefaultContext implements OwnableContext
{
    protected Map<String, Property> properties;

    protected Map<String, Object> attributes;

    protected Contextable owner;

    protected Executor executor;

    public DefaultContext()
    {
        this(new LinkedHashMap<String, Property>(), new HashMap<String, Object>());
    }

    public DefaultContext(Map<String, Property> properties, Map<String, Object> attributes)
    {
        this(properties, attributes, Executor.getExecutor());
    }

    public DefaultContext(Map<String, Property> properties, Map<String, Object> attributes, Executor executor)
    {
        this.properties = properties;
        this.attributes = attributes;
        this.executor = executor;
    }

    @Override
    public Collection<String> getPropertyNames()
    {
        return this.properties != null ? this.properties.keySet() : null;
    }

    @Override
    public String getProperty(String name)
    {
        // Find property
        Property property = this.properties != null ? this.properties.get(name) : null;
        if (property == null)
        {
            // Return null if property is not found
            return null;
        }

        // Resolve found property value
        String propertyValue = null;
        try
        {
            propertyValue = property.getValue(RunContext.builder().withHostName(this.getHostName())
                    .withPid(this.getPid()).withThreadName(this.getThreadName()).withRunNumber(this.getRunNumber())
                    .build());
        }
        catch (PropertyException e)
        {
            throw new IllegalStateException("Context error resolving property!", e);
        }

        return propertyValue;
    }

    @Override
    public Collection<String> findPropertyNames()
    {
        // Get all property names from this context
        Collection<String> propertyNames = new TreeSet<String>(this.getPropertyNames());

        // If there is parent context, add property names from parent context too
        Context parentContext = this.getParentContext();
        if (parentContext != null)
        {
            propertyNames.addAll(parentContext.findPropertyNames());
        }

        return propertyNames;
    }

    @Override
    public String findProperty(String name)
    {
        // Find property in this context
        String property = this.getProperty(name);
        if (property == null)
        {
            // If property is not found in this context, try to find in parent context
            Context parentContext = this.getParentContext();
            if (parentContext != null)
            {
                property = parentContext.findProperty(name);
            }
        }

        return property;
    }

    @Override
    public Collection<String> getAttributeNames()
    {
        return this.attributes != null ? this.attributes.keySet() : null;
    }

    @Override
    public Object getAttribute(String name)
    {
        return this.attributes != null ? this.attributes.get(name) : null;
    }

    @Override
    public void setAttribute(String name, Object o)
    {
        if (this.attributes == null) 
        {
            this.attributes = new HashMap<String, Object>();
        } 
        this.attributes.put(name, o);
    }

    @Override
    public Collection<String> findAttributeNames()
    {
        // Get all attribute names from this context
        Collection<String> attributeNames = new TreeSet<String>(this.getAttributeNames());

        // If there is parent context, add attribute names from parent context too
        Context parentContext = this.getParentContext();
        if (parentContext != null)
        {
            attributeNames.addAll(parentContext.findAttributeNames());
        }

        return attributeNames;
    }

    @Override
    public Object findAttribute(String name)
    {
        // Find attribute in this context
        Object attribute = this.attributes != null ? this.attributes.get(name) : null;
        if (attribute == null)
        {
            // If attribute is not found in this context, try to find in parent context
            Context parentContext = this.getParentContext();
            if (parentContext != null)
            {
                attribute = parentContext.findAttribute(name);
            }
        }

        return attribute;
    }

    @Override
    public Logger getLogger()
    {
        return this.executor != null ? this.executor.getLogger() : null;
    }

    @Override
    public Context getParentContext()
    {
        // Context by itself shouldn't be created in hierarchical object structure,
        // but owning elements most probably are.
        // Try to use owning element to traverse through hierarchy and to find parent element of owning element.
        // If such element is found, use its context as parent context to this context
        return (this.owner != null && this.owner.getParent() != null) ? this.owner.getParent().getContext() : null;
    }

    @Override
    public String getHostName()
    {
        return this.executor != null ? this.executor.getInfo().getHostName() : null;
    }

    @Override
    public String getPid()
    {
        return this.executor != null ? this.executor.getInfo().getPid() : null;
    }

    @Override
    public String getThreadName()
    {
        return this.executor != null ? this.executor.getInfo().getThreadName() : null;
    }

    @Override
    public int getRunNumber()
    {
        return this.executor != null ? this.executor.getInfo().getRunNumber() : -1;
    }

    @Override
    public Contextable getOwner()
    {
        return this.owner;
    }

    @Override
    public void setOwner(Contextable owner)
    {
        this.owner = owner;
    }

    @Override
    public Property getPropertyObject(String name)
    {
        return this.properties != null ? this.properties.get(name) : null;
    }

    @Override
    public Property findPropertyObject(String name)
    {
        // Find property in this context
        Property property = this.getPropertyObject(name);
        if (property == null)
        {
            // If property is not found in this context, try to find in parent context
            OwnableContext parentContext = this.owner != null && this.owner.getParent() != null ? 
                    this.owner.getParent().getContext() : null;
            if (parentContext != null)
            {
                property = parentContext.findPropertyObject(name);
            }
        }

        return property;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n   class: ").append(getClass().getName()).append("\n   ");
        if (properties != null)
        {
            builder.append("properties: ").append(properties.toString().replaceFirst("\\{", "\\{\n").replaceAll("(.*)", "      $1").trim()).append("\n   ");
        }
        if (attributes != null)
        {
            builder.append("attributes: ").append(attributes.toString().replaceFirst("\\{", "\\{\n").replaceAll("(.*)", "      $1").trim()).append("\n   ");
        }
        if (owner != null)
        {
            builder.append("owner: ").append("...");
        }
        builder.append("\n}");
        return builder.toString();
    }

}
