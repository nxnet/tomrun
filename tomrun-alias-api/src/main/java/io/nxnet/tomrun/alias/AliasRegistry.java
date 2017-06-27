package io.nxnet.tomrun.alias;

import java.util.Set;

import io.nxnet.tomrun.service.loader.NamedService;

public interface AliasRegistry extends NamedService
{
    String getName(String namespace, String alias) throws AliasException;

    Set<String> getAliases(String namespace, String name) throws AliasException;

    void setAliases(String namespace, String name, Set<String> aliases) throws AliasException;

    void registerAlias(String namespace, String name, String alias) throws AliasException;

    boolean unregisterAlias(String namespace, String name, String alias) throws AliasException;
}
