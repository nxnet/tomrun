package io.nxnet.tomrun.alias;

import io.nxnet.tomrun.service.loader.NamedService;
import io.nxnet.tomrun.service.loader.NamedServiceLoader;

public abstract class AliasRegistryFactory implements NamedService
{
    /**
     * Default factory name which will be used if no factory name is provided.
     */
    public static final String DEFAULT_FACTORY_NAME = "defaultAliasRegistryFactory";

    public abstract AliasRegistry getAliasRegistry();

    public abstract AliasRegistry getAliasRegistry(String registryName);

    public abstract AliasRegistry getAliasRegistry(ClassLoader classLoader);

    public abstract AliasRegistry getAliasRegistry(String registryName, ClassLoader classLoader);

    /**
     * Instantiate factory using default class loader.
     * 
     * @return
     */
    public static AliasRegistryFactory newInstance()
    {
        return newInstance(DEFAULT_FACTORY_NAME, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Instantiate factory using provided factory name and default class loader.
     * 
     * @return
     */
    public static AliasRegistryFactory newInstance(String factoryName)
    {
        return newInstance(factoryName, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Instantiate factory using provided class loader.
     * 
     * @return
     */
    public static AliasRegistryFactory newInstance(ClassLoader classLoader)
    {
        return newInstance(DEFAULT_FACTORY_NAME, classLoader);
    }

    /**
     * Instantiate factory using provided factory name and class loader.
     * 
     * @return
     */
    public static AliasRegistryFactory newInstance(String factoryName, ClassLoader classLoader)
    {
        // Search for factories
        return NamedServiceLoader.getInstance().loadService(factoryName, AliasRegistryFactory.class, classLoader);
    }
}
