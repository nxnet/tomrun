package io.nxnet.tomrun.parser;

import io.nxnet.tomrun.model.AbstractAction;

public interface ActionIterator extends ExecutionElementIterator<AbstractAction>
{
    public AbstractAction next() throws TomConfigurationError;
    
    public boolean hasNext();
}
