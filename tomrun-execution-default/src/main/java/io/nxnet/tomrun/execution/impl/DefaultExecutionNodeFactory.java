package io.nxnet.tomrun.execution.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nxnet.tomrun.context.ContextException;
import io.nxnet.tomrun.context.ContextFactory;
import io.nxnet.tomrun.context.OwnableContext;
import io.nxnet.tomrun.execution.DynamicIncludeNode;
import io.nxnet.tomrun.execution.ExecutionFilter;
import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeException;
import io.nxnet.tomrun.execution.ExecutionNodeFactory;
import io.nxnet.tomrun.execution.ExecutionNodeType;
import io.nxnet.tomrun.model.Properties;
import io.nxnet.tomrun.model.Wrapper;
import io.nxnet.tomrun.model.WrapperAwareElement;

import io.nxnet.tomrun.execution.utils.DynamicIncludeElementWrapper;

public class DefaultExecutionNodeFactory extends ExecutionNodeFactory
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String dynamicIncludeFile = "tom.xml";

    @Override
    public String getServiceName()
    {
        return DEFAULT_FACTORY_NAME;
    }

    @Override
    public ExecutionNode getNode(WrapperAwareElement nodeElement, ExecutionFilter filter) throws ExecutionNodeException
    {
        // Validate args
        if (nodeElement == null)
        {
            throw new IllegalArgumentException("node element is null");
        }

        if (nodeElement.getDynamicInclude() == null)
        {
            return getNodeInternal(nodeElement, filter);
        }
        else
        {
            return getNodeProxy(nodeElement, filter);
        }
    }

    public String getDynamicIncludeFile()
    {
        return dynamicIncludeFile;
    }

    public void setDynamicIncludeFile(String dynamicIncludeFile)
    {
        this.dynamicIncludeFile = dynamicIncludeFile;
    }

    protected ExecutionNode getNodeInternal(WrapperAwareElement nodeElement, ExecutionFilter filter) throws ExecutionNodeException
    {
        ExecutionNode executionNode = null;
        if (filter == null || filter.matches(nodeElement))
        {
            logger.trace("Creating execution node instance from element: {}", nodeElement);

            // Create node instance
            executionNode = new ExecutionNodeImpl(nodeElement.getId(), nodeElement.getName(),
                    nodeElement.getDescription(), ExecutionNodeType.getEnum(nodeElement.getType()));

            // Set context
            OwnableContext context = null;
            Properties properties = nodeElement.getProperties();
            String contextFactoryName = properties != null && properties.getFactory() != null ? 
                    properties.getFactory() : ContextFactory.DEFAULT_FACTORY_NAME;
            try
            {   
                context = ContextFactory.newInstance(contextFactoryName).getContext(properties);
            }
            catch (ContextException e)
            {
                throw new ExecutionNodeException("Error creating context for properties: " + properties, e);
            }
            executionNode.setContext(context);
    
            // Set children
            boolean hasChildren = false;
            if (nodeElement.getChildren() != null)
            {
                // Add execution node to child list
                for (WrapperAwareElement childElement : nodeElement.getChildren())
                {
                    if (childElement != null)
                    {
                        // Use child execution node factory...
                        String childExecutionNodeFactory = childElement.getFactory() != null ?
                                childElement.getFactory() : ExecutionNodeFactory.DEFAULT_FACTORY_NAME;
                        ExecutionNode childExecutionNode = ExecutionNodeFactory.newInstance(childExecutionNodeFactory)
                                .getNode(childElement, filter);

                        // Add child execution node
                        if (childExecutionNode != null)
                        {
                            executionNode.addChild(childExecutionNode);
                            childExecutionNode.setParent(executionNode);
                            hasChildren = true;
                        }
                    }
                }
            }

            if (hasChildren)
            {       
                // Set before wrapper
                Wrapper beforeFirstWrapper = nodeElement.getBeforeFirst();
                if (beforeFirstWrapper != null)
                {
                    // Use before first execution node factory...
                    String beforeFirstExecutionNodeFactory = beforeFirstWrapper.getFactory() != null ?
                            beforeFirstWrapper.getFactory() : ExecutionNodeFactory.DEFAULT_FACTORY_NAME;
                    ExecutionNode beforeFirstExecutionNode = ExecutionNodeFactory.newInstance(
                            beforeFirstExecutionNodeFactory).getNode(beforeFirstWrapper, filter);

                    // Add before first node
                    if (beforeFirstExecutionNode != null)
                    {
                        executionNode.setBefore(beforeFirstExecutionNode);
                        beforeFirstExecutionNode.setParent(executionNode);
                    }
                }

                // Set after wrapper
                Wrapper afterLastWrapper = nodeElement.getAfterLast();
                if (afterLastWrapper != null)
                {
                    // Use after last execution node factory...
                    String afterLastExecutionNodeFactory = afterLastWrapper.getFactory() != null ?
                            afterLastWrapper.getFactory() : ExecutionNodeFactory.DEFAULT_FACTORY_NAME;
                    ExecutionNode afterLastExecutionNode = ExecutionNodeFactory.newInstance(
                            afterLastExecutionNodeFactory).getNode(afterLastWrapper, filter);

                    // Add after last node
                    if (afterLastExecutionNode != null)
                    {
                        executionNode.setAfter(afterLastExecutionNode);
                        afterLastExecutionNode.setParent(executionNode);
                    }
                }

                // Set child's before wrapper
                Wrapper beforeEachWrapper = nodeElement.getBeforeEach();
                if (beforeEachWrapper != null)
                {
                    // Use before each execution node factory...
                    String beforeEachExecutionNodeFactory = beforeEachWrapper.getFactory() != null ?
                            beforeEachWrapper.getFactory() : ExecutionNodeFactory.DEFAULT_FACTORY_NAME;
                    ExecutionNode beforeEachExecutionNode = ExecutionNodeFactory.newInstance(
                            beforeEachExecutionNodeFactory).getNode(beforeEachWrapper, filter);

                    // Add before each node
                    if (beforeEachExecutionNode != null)
                    {
                        executionNode.setBeforeChild(beforeEachExecutionNode);
                        beforeEachExecutionNode.setParent(executionNode);
                    }
                }

                // Set child's after wrapper
                Wrapper afterEachWrapper = nodeElement.getAfterEach();
                if (afterEachWrapper != null)
                {
                    // Use after each execution node factory...
                    String afterEachExecutionNodeFactory = afterEachWrapper.getFactory() != null ?
                            afterEachWrapper.getFactory() : ExecutionNodeFactory.DEFAULT_FACTORY_NAME;
                    ExecutionNode afterEachExecutionNode = ExecutionNodeFactory.newInstance(
                            afterEachExecutionNodeFactory).getNode(afterEachWrapper, filter);

                    // Add after each node
                    if (afterEachExecutionNode != null)
                    {
                        executionNode.setAfterChild(afterEachExecutionNode);
                        afterEachExecutionNode.setParent(executionNode);
                    }
                }
            }
        }

        logger.trace("Execution node instance created: {}", executionNode);
        return executionNode;
    }

    protected ExecutionNode getNodeProxy(WrapperAwareElement nodeElement, ExecutionFilter filter)
    {
        ExecutionNodeProxy executionNode = null;
        if (filter == null || filter.matches(nodeElement))
        {
            // Create execution node instance
            executionNode = new ExecutionNodeProxy(nodeElement.getId(), nodeElement.getName(),
                    nodeElement.getDescription(), ExecutionNodeType.getEnum(nodeElement.getType()));
            
            // Create dynamic include node instance
            DynamicIncludeNode dynamicIncludeNode = new DynamicIncludeElementWrapper(
                    nodeElement.getDynamicInclude()).toNode();
            
            // Set dynamic include node instance
            executionNode.setDynamicIncludeNode(dynamicIncludeNode);
        }
        
        return executionNode;
    }

}
