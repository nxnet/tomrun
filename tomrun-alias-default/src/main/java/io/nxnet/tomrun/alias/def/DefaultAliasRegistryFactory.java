package io.nxnet.tomrun.alias.def;

import io.nxnet.tomrun.alias.AliasRegistry;
import io.nxnet.tomrun.alias.AliasRegistryFactory;
import io.nxnet.tomrun.service.loader.NamedServiceLoader;

public class DefaultAliasRegistryFactory extends AliasRegistryFactory
{
    public static final String DEFAULT_REGISTRY_NAME = "defaultRegistry";

    @Override
    public AliasRegistry getAliasRegistry()
    {
        return this.getAliasRegistry(DEFAULT_REGISTRY_NAME, Thread.currentThread().getContextClassLoader());
    }

    @Override
    public AliasRegistry getAliasRegistry(String registryName)
    {
        return this.getAliasRegistry(registryName, Thread.currentThread().getContextClassLoader());
    }

    @Override
    public AliasRegistry getAliasRegistry(ClassLoader classLoader)
    {
        return this.getAliasRegistry(DEFAULT_REGISTRY_NAME, classLoader);
    }

    @Override
    public AliasRegistry getAliasRegistry(String registryName, ClassLoader classLoader)
    {
        return NamedServiceLoader.getInstance().loadService(registryName, AliasRegistry.class, classLoader);
    }

    @Override
    public String getServiceName()
    {
        return AliasRegistryFactory.DEFAULT_FACTORY_NAME;
    }

}
