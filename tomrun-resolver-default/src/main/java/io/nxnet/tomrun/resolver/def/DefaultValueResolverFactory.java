package io.nxnet.tomrun.resolver.def;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import io.nxnet.tomrun.alias.AliasException;
import io.nxnet.tomrun.alias.AliasRegistry;
import io.nxnet.tomrun.alias.AliasRegistryFactory;
import io.nxnet.tomrun.resolver.ValueResolver;
import io.nxnet.tomrun.resolver.ValueResolverException;
import io.nxnet.tomrun.resolver.ValueResolverFactory;

public class DefaultValueResolverFactory extends ValueResolverFactory
{
    public static final String DEFAULT_ALIAS_NAMESPACE = "valueResolver";

    public static final String DEFAULT_VALUE_RESOLVER_NAME = LiteralResolver.class.getName();

    private AliasRegistryFactory aliasRegistryFactory;

    private String aliasNamespace;

    private String defaulValueResolverName;

    public DefaultValueResolverFactory()
    {
        this(AliasRegistryFactory.newInstance());
    }

    public DefaultValueResolverFactory(AliasRegistryFactory aliasRegistryFactory)
    {
        this(aliasRegistryFactory, DEFAULT_ALIAS_NAMESPACE, DEFAULT_VALUE_RESOLVER_NAME);
    }

    public DefaultValueResolverFactory(AliasRegistryFactory aliasRegistryFactory, String aliasNamespace,
            String defaulValueResolverName)
    {
        this.aliasRegistryFactory = aliasRegistryFactory;
        this.aliasNamespace = aliasNamespace;
        this.defaulValueResolverName = defaulValueResolverName;
    }

    @Override
    public ValueResolver getValueResolver(String valueResolver, Object... args) throws ValueResolverException
    {
        if (valueResolver == null)
        {
            valueResolver = this.defaulValueResolverName;
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<ValueResolver> valueResolverClass = null;
        try
        {
            valueResolverClass = (Class<ValueResolver>) classLoader.loadClass(valueResolver);
        }
        catch (ClassNotFoundException e)
        {
            // If alias is used instead of fully qualified class name
            AliasRegistry aliasRegistry = this.aliasRegistryFactory.getAliasRegistry();
            String realClassName = null;
            try
            {
                realClassName = aliasRegistry.getName(this.aliasNamespace, valueResolver);//getAlias(valueResolver);
            }
            catch (AliasException e2)
            {
                throw new ValueResolverException("Can't find value resolver class " + valueResolver, e2);
            }
            
            if (realClassName != null)
            {
                try
                {
                    valueResolverClass = (Class<ValueResolver>) classLoader.loadClass(realClassName);
                }
                catch (ClassNotFoundException e1)
                {
                    throw new ValueResolverException("Can't find value resolver class " + realClassName, e1);
                }
            }
            else
            {
                throw new ValueResolverException("Can't find value resolver class " + valueResolver, e);
            }
        }

        // Initialize constructor argument types
        Class[] argsClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++)
        {
            if (args[i] != null)
            {
                argsClasses[i] = args[i].getClass();
            }
            else
            {
                throw new IllegalArgumentException(MessageFormat
                        .format("Value resolver constructor argument {0} is null.", i));
            }
        }
        
        // Instantiate value resolver
        ValueResolver valueResolverInstance = null;
        try
        {
            valueResolverInstance = valueResolverClass.getConstructor(argsClasses).newInstance(args);
        }
        catch (InstantiationException e)
        {
            throw new ValueResolverException("Can't instantate value resolver " + valueResolver, e);
        }
        catch (IllegalAccessException e)
        {
            throw new ValueResolverException("Can't instantate value resolver " + valueResolver, e);
        }
        catch (IllegalArgumentException e)
        {
            throw new ValueResolverException("Can't instantate value resolver " + valueResolver, e);
        }
        catch (InvocationTargetException e)
        {
            throw new ValueResolverException("Can't instantate value resolver " + valueResolver, e);
        }
        catch (NoSuchMethodException e)
        {
            throw new ValueResolverException("Can't instantate value resolver " + valueResolver, e);
        }
        catch (SecurityException e)
        {
            throw new ValueResolverException("Can't instantate value resolver " + valueResolver, e);
        }

        return valueResolverInstance;
    }

    @Override
    public String getServiceName()
    {
        return ValueResolverFactory.DEFAULT_FACTORY_NAME;
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
     * @return the aliasNamespace
     */
    public String getAliasNamespace()
    {
        return aliasNamespace;
    }

    /**
     * @param aliasNamespace the aliasNamespace to set
     */
    public void setAliasNamespace(String aliasNamespace)
    {
        this.aliasNamespace = aliasNamespace;
    }

}
