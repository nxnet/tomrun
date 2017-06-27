package io.nxnet.tomrun.context;

import io.nxnet.tomrun.model.Properties;
import io.nxnet.tomrun.service.loader.NamedService;
import io.nxnet.tomrun.service.loader.NamedServiceLoader;

public abstract class ContextFactory implements NamedService
{
    /**
     * Default factory name which will be used if no factory name is provided.
     */
    public static final String DEFAULT_FACTORY_NAME = "defaultContextFactory";

    public abstract OwnableContext getContext(Properties properties) throws ContextException;

    /**
     * Instantiate factory using default class loader.
     * 
     * @return
     */
    public static ContextFactory newInstance()
    {
        return newInstance(DEFAULT_FACTORY_NAME, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Instantiate factory using provided factory name and default class loader.
     * 
     * @return
     */
    public static ContextFactory newInstance(String factoryName)
    {
        return newInstance(factoryName, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Instantiate factory using provided class loader.
     * 
     * @return
     */
    public static ContextFactory newInstance(ClassLoader classLoader)
    {
        return newInstance(DEFAULT_FACTORY_NAME, classLoader);
    }

    /**
     * Instantiate factory using provided factory name and class loader.
     * 
     * @return
     */
    public static ContextFactory newInstance(String factoryName, ClassLoader classLoader)
    {
        // Search for factories
        return NamedServiceLoader.getInstance().loadService(factoryName, ContextFactory.class, classLoader);
    }

}