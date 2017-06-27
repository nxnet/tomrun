package io.nxnet.tomrun.parser;

import io.nxnet.tomrun.model.WrapperAwareElement;

public interface ExecutionElementIterator<E extends WrapperAwareElement>
{
    public E next() throws TomConfigurationError;
    
    public boolean hasNext();
}
