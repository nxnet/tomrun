package io.nxnet.tomrun.property.def;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.nxnet.tomrun.alias.AliasRegistryFactory;
import io.nxnet.tomrun.interpolation.InterpolationStrategy;
import io.nxnet.tomrun.property.Property;
import io.nxnet.tomrun.property.PropertyException;
import io.nxnet.tomrun.property.PropertyFactory;
import io.nxnet.tomrun.resolver.ResolveStrategy;
import io.nxnet.tomrun.resolver.ValueResolver;
import io.nxnet.tomrun.resolver.ValueResolverException;
import io.nxnet.tomrun.resolver.ValueResolverFactory;

public class DefaultPropertyFactory extends PropertyFactory
{
    public static final String DEFAULT_PROPERTY_DECORATOR_ALIAS_NAMESPACE = "propertyDecorator";

    private ValueResolverFactory valueResolverFactory;

    private AliasRegistryFactory aliasRegistryFactory;

    private String propertyDecoratorAliasNamespace;

    private ResolveStrategy defaultResolveStrategy;

    public DefaultPropertyFactory()
    {
        this(ValueResolverFactory.newInstance(), AliasRegistryFactory.newInstance(), 
                DEFAULT_PROPERTY_DECORATOR_ALIAS_NAMESPACE);
    }

    public DefaultPropertyFactory(ValueResolverFactory valueResolverFactory, AliasRegistryFactory aliasRegistryFactory,
            String propertyDecoratorAliasNamespace)
    {
        this.valueResolverFactory = valueResolverFactory;
        this.aliasRegistryFactory = aliasRegistryFactory;
        this.propertyDecoratorAliasNamespace = propertyDecoratorAliasNamespace;
        this.defaultResolveStrategy = ResolveStrategy.PER_RUN;
    }

    @Override
    public Property getProperty(io.nxnet.tomrun.model.Property propertyElement) throws PropertyException
    {
        // Validate args
        if (propertyElement == null)
        {
            throw new IllegalArgumentException("property element is null");
        }

        // Extract values from element
        String name = propertyElement.getName();
        String value = propertyElement.getValue();
        String valueResolver = propertyElement.getValueResolver(); 
        List<String> decorators = propertyElement.getFilters() != null ? 
                Arrays.asList(propertyElement.getFilters().split(",")) : new ArrayList<String>();

        // Determine resolve strategy
        InterpolationStrategy interpolationStrategy = InterpolationStrategy.parseFrom(
                propertyElement.getInterpolationStrategy());

        // Determine resolve strategy
        ResolveStrategy resolveStrategy = this.getResolveStrategy(propertyElement.getResolveStrategy(),
                this.defaultResolveStrategy);

        // Validate args 2nd phase
        if (name == null)
        {
            throw new IllegalArgumentException("property element name is null for property element:\n"
                    + propertyElement);
        }

        // Create value resolver
        ValueResolver resolver = null;
        try
        {
            if (value == null)
            {
                resolver = this.valueResolverFactory.getValueResolver(valueResolver);
            }
            else
            {
                resolver = this.valueResolverFactory.getValueResolver(valueResolver, value);
            }
        }
        catch (ValueResolverException e)
        {
            throw new PropertyException("Error creating resolver '" + valueResolver 
                    + "' for property: " + propertyElement, e);
        }

        // Create property
        Property property = null;
        switch (resolveStrategy)
        {
            case ONCE:
                property = new GloballyScopedProperty(new DefaultProperty(name, resolver, interpolationStrategy));
                break;
            case PER_HOST:
                property = new HostScopedProperty(new DefaultProperty(name, resolver, interpolationStrategy));
                break;
            case PER_PROCESS:
                property = new ProcessScopedProperty(new DefaultProperty(name, resolver, interpolationStrategy));
                break;
            case PER_THREAD:
                property = new ThreadScopedProperty(new DefaultProperty(name, resolver, interpolationStrategy));
                break;
            case PER_RUN:
                property = new RunScopedProperty(new DefaultProperty(name, resolver, interpolationStrategy));
                break;
            case ALWAYS:
                property = new DefaultProperty(name, resolver, interpolationStrategy);
                break;
            default:
                break;
        }

        // Return decorated property
        return new PropertyDecoratorBuilder().withAliasRegistry(this.aliasRegistryFactory.getAliasRegistry())
                .withAliasNamespace(this.propertyDecoratorAliasNamespace).withDecorators(decorators)
                .withTarget(property).build();
    }

    @Override
    public String getServiceName()
    {
        return PropertyFactory.DEFAULT_FACTORY_NAME;
    }

    /**
     * @param valueResolverFactory the valueResolverFactory to set
     */
    public void setValueResolverFactory(ValueResolverFactory valueResolverFactory)
    {
        this.valueResolverFactory = valueResolverFactory;
    }

    /**
     * @return the valueResolverFactory
     */
    public ValueResolverFactory getValueResolverFactory()
    {
        return this.valueResolverFactory;
    }

    /**
     * @return the aliasRegistryFactory
     */
    public AliasRegistryFactory getAliasRegistryFactory()
    {
        return aliasRegistryFactory;
    }

    /**
     * @param aliasRegistryFactory the aliasRegistryFactory to set
     */
    public void setAliasRegistryFactory(AliasRegistryFactory aliasRegistryFactory)
    {
        this.aliasRegistryFactory = aliasRegistryFactory;
    }

    /**
     * @return the propertyDecoratorAliasNamespace
     */
    public String getPropertyDecoratorAliasNamespace()
    {
        return propertyDecoratorAliasNamespace;
    }

    /**
     * @param propertyDecoratorAliasNamespace the propertyDecoratorAliasNamespace to set
     */
    public void setPropertyDecoratorAliasNamespace(String propertyDecoratorAliasNamespace)
    {
        this.propertyDecoratorAliasNamespace = propertyDecoratorAliasNamespace;
    }

    /**
     * @return the defaultResolveStrategy
     */
    public ResolveStrategy getDefaultResolveStrategy()
    {
        return defaultResolveStrategy;
    }

    /**
     * @param defaultResolveStrategy the defaultResolveStrategy to set
     */
    public void setDefaultResolveStrategy(ResolveStrategy defaultResolveStrategy)
    {
        this.defaultResolveStrategy = defaultResolveStrategy;
    }

    String camel2snake(String camel)
    {
        if (camel == null)
        {
            return null;
        }
        return camel.replaceAll("([a-z])([A-Z]+)", "$1_$2");
    }


    String toUpper(String lower)
    {
        if (lower == null)
        {
            return null;
        }
        return lower.toUpperCase();
    }

    ResolveStrategy getResolveStrategy(String strategyName, ResolveStrategy defaultStrategy)
    {
        return strategyName != null ? ResolveStrategy.valueOf(this.toUpper(this.camel2snake(strategyName))) :
                    defaultStrategy;
    }

}
