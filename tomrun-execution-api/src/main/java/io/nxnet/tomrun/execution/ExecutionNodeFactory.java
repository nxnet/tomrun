package io.nxnet.tomrun.execution;

import io.nxnet.tomrun.model.WrapperAwareElement;
import io.nxnet.tomrun.service.loader.NamedService;
import io.nxnet.tomrun.service.loader.NamedServiceLoader;

public abstract class ExecutionNodeFactory implements NamedService
{
    public static final String DEFAULT_FACTORY_NAME = "defaultExecutionNodeFactory";

    /**
     * Factory method which produces {@link ExecutionNode} instance.
     * 
     * @param nodeElement
     * @param filter
     * @return
     * @throws ExecutionNodeException
     */
    public ExecutionNode getNode(WrapperAwareElement nodeElement) throws ExecutionNodeException
    {
        return this.getNode(nodeElement, ExecutionFilter.MATCH_ALL);
    }

    /**
     * Factory method which produces {@link ExecutionNode} instance based on given filter.
     * 
     * @param nodeElement
     * @param filter
     * @return
     * @throws ExecutionNodeException
     */
    public abstract ExecutionNode getNode(WrapperAwareElement nodeElement, ExecutionFilter filter)
            throws ExecutionNodeException;

    /**
     * Instantiate factory using default class loader.
     * 
     * @return
     */
    public static ExecutionNodeFactory newInstance()
    {
        return newInstance(DEFAULT_FACTORY_NAME, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Instantiate factory using provided factory name and default class loader.
     * 
     * @return
     */
    public static ExecutionNodeFactory newInstance(String factoryName)
    {
        return newInstance(factoryName, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Instantiate factory using provided class loader.
     * 
     * @return
     */
    public static ExecutionNodeFactory newInstance(ClassLoader classLoader)
    {
        return newInstance(DEFAULT_FACTORY_NAME, classLoader);
    }

    /**
     * Instantiate factory using provided factory name and class loader.
     * 
     * @return
     */
    public static ExecutionNodeFactory newInstance(String factoryName, ClassLoader classLoader)
    {
        // Search for factories
        return NamedServiceLoader.getInstance().loadService(factoryName, ExecutionNodeFactory.class, classLoader);
    }

}
