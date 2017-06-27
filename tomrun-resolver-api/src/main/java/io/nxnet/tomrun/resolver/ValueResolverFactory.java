package io.nxnet.tomrun.resolver;

import io.nxnet.tomrun.service.loader.NamedService;
import io.nxnet.tomrun.service.loader.NamedServiceLoader;

public abstract class ValueResolverFactory implements NamedService
{
    /**
     * Default factory name which will be used if no factory name is provided.
     */
    public static final String DEFAULT_FACTORY_NAME = "defaultValueResolverFactory";

    public abstract ValueResolver getValueResolver(String valueResolver, Object... args)
            throws ValueResolverException;

    /**
     * Instantiate factory using default class loader.
     * 
     * @return
     */
    public static ValueResolverFactory newInstance()
    {
        return newInstance(DEFAULT_FACTORY_NAME, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Instantiate factory using provided factory name and default class loader.
     * 
     * @return
     */
    public static ValueResolverFactory newInstance(String factoryName)
    {
        return newInstance(factoryName, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Instantiate factory using provided class loader.
     * 
     * @return
     */
    public static ValueResolverFactory newInstance(ClassLoader classLoader)
    {
        return newInstance(DEFAULT_FACTORY_NAME, classLoader);
    }

    /**
     * Instantiate factory using provided factory name and class loader.
     * 
     * @return
     */
    public static ValueResolverFactory newInstance(String factoryName, ClassLoader classLoader)
    {
        // Search for factories
        return NamedServiceLoader.getInstance().loadService(factoryName, ValueResolverFactory.class, classLoader);
    }
}
