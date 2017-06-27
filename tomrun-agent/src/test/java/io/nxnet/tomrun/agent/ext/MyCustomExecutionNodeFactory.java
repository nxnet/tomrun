package io.nxnet.tomrun.agent.ext;

import io.nxnet.tomrun.execution.ExecutionFilter;
import io.nxnet.tomrun.execution.ExecutionNode;
import io.nxnet.tomrun.execution.ExecutionNodeException;
import io.nxnet.tomrun.execution.impl.DefaultExecutionNodeFactory;
import io.nxnet.tomrun.model.WrapperAwareElement;

public class MyCustomExecutionNodeFactory extends DefaultExecutionNodeFactory {

	@Override
	public ExecutionNode getNode(WrapperAwareElement nodeElement,
			ExecutionFilter filter) throws ExecutionNodeException {
		
		if ("P".equals(nodeElement.getType()))
		{
			return super.getNode(nodeElement, filter);
		}
		return null;
	}

	
}
