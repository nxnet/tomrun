package io.nxnet.tomrun.execution.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.nxnet.tomrun.execution.ExecutionFilter;
import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeException;
import io.nxnet.tomrun.execution.ExecutionNodeFactory;
import io.nxnet.tomrun.model.WrapperAwareElement;

public class SpringExecutionNodeFactory extends ExecutionNodeFactory
{
    /**
     * Factory method which produces {@link ExecutionNode} instance.
     * 
     * @param nodeElement
     * @param filter
     * @return
     * @throws ExecutionNodeException
     */
    public ExecutionNode getNode(WrapperAwareElement nodeElement, ExecutionFilter filter) throws ExecutionNodeException
    {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath*:tomrun-application-context.xml");

        return context.getBean(nodeElement.getFactory(), ExecutionNode.class);
    }

    @Override
    public String getServiceName()
    {
        return "springExecutionNodeFactory";
    }

}
