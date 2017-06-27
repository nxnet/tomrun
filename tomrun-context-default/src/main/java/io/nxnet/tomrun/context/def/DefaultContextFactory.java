package io.nxnet.tomrun.context.def;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.nxnet.tomrun.context.ContextException;
import io.nxnet.tomrun.context.ContextFactory;
import io.nxnet.tomrun.context.OwnableContext;
import io.nxnet.tomrun.executor.Executor;
import io.nxnet.tomrun.model.Properties;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;
import io.nxnet.tomrun.property.PropertyFactory;

public class DefaultContextFactory extends ContextFactory
{
    private PropertyFactory propertyFactory;

    public DefaultContextFactory()
    {
        this.propertyFactory = PropertyFactory.newInstance();
    }

    public DefaultContextFactory(PropertyFactory propertyFactory)
    {
        this.propertyFactory = propertyFactory;
    }

    @Override
    public OwnableContext getContext(Properties propertiesElement) throws ContextException
    {
        // Instantiate properties map
        Map<String, Property> properties = new HashMap<String, Property>();

        // Instantiate context
        DefaultContext context = new DefaultContext(properties, new HashMap<String, Object>(), Executor.getExecutor());

        // Validate args
        if (propertiesElement != null)
        {
            // Populate properties map
            Property property = null;
            Set<io.nxnet.tomrun.model.Property> propertyElementSet = propertiesElement.getProperties();
            if (propertyElementSet != null)
            {
                for (io.nxnet.tomrun.model.Property propertyElement : propertyElementSet)
                {
                    // Create property instance
                    try
                    {
                        property = this.propertyFactory.getProperty(propertyElement);
                    }
                    catch (PropertyException e)
                    {
                        throw new ContextException("Error creating property: " + propertyElement, e);
                    }
                    
                    // Put property instance into context
                    properties.put(property.getName(), new InterpolatableProperty(property, context));
                }
            }
        }

        return context;
    }

    @Override
    public String getServiceName()
    {
        return DEFAULT_FACTORY_NAME;
    }

    public PropertyFactory getPropertyFactory()
    {
        return propertyFactory;
    }

    public void setPropertyFactory(PropertyFactory propertyFactory)
    {
        this.propertyFactory = propertyFactory;
    }

}
