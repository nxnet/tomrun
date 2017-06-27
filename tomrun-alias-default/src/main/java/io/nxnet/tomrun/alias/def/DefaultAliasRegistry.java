package io.nxnet.tomrun.alias.def;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.nxnet.tomrun.alias.AliasException;
import io.nxnet.tomrun.alias.AliasRegistry;

public class DefaultAliasRegistry implements AliasRegistry
{
    private Map<String, Set<String>> aliases;

    private Map<String, String> names;

    public DefaultAliasRegistry()
    {
        this.aliases = new HashMap<String, Set<String>>();
        this.names = new HashMap<String, String>();
    }

    @Override
    public String getServiceName()
    {
        return DefaultAliasRegistryFactory.DEFAULT_REGISTRY_NAME;
    }

    @Override
    public String getName(String namespace, String alias) throws AliasException
    {
        return this.names.get(namespace + alias);
    }

    @Override
    public Set<String> getAliases(String namespace, String name) throws AliasException
    {
        return this.aliases.get(namespace + name);
    }

    @Override
    public void registerAlias(String namespace, String name, String alias) throws AliasException
    {
        Set<String> aliases = this.getAliases(namespace, name);
        if (aliases == null)
        {
            aliases = new HashSet<String>();
            this.setAliases(namespace, name, aliases);
        }
        aliases.add(alias);
        this.names.put(namespace + alias, name);
    }

    @Override
    public boolean unregisterAlias(String namespace, String name, String alias) throws AliasException
    {
        this.names.remove(namespace + alias);
        Set<String> aliases = this.getAliases(namespace, name);
        return aliases.remove(alias);
    }

    @Override
    public void setAliases(String namespace, String name, Set<String> aliases) throws AliasException
    {
        this.aliases.put(namespace + name, aliases);
        for (String alias : aliases)
        {
            this.names.put(namespace + alias, name);
        }
    }

}
