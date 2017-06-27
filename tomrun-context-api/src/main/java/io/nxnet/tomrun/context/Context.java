package io.nxnet.tomrun.context;

import java.util.Collection;

import org.slf4j.Logger;

public interface Context
{
    Collection<String> getPropertyNames();

    String getProperty(String name);

    Collection<String> findPropertyNames();

    String findProperty(String name);

    Collection<String> getAttributeNames();

    Object getAttribute(String name);

    void setAttribute(String name, Object o);

    Collection<String> findAttributeNames();

    Object findAttribute(String name);

    Context getParentContext();

    Logger getLogger();

    String getHostName();

    String getPid();
 
    String getThreadName();

    int getRunNumber();

}
