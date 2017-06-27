package io.nxnet.tomrun.parser;

import io.nxnet.tomrun.model.Wrapper;

public interface WrapperIterator extends ExecutionElementIterator<Wrapper>
{
    public Wrapper next() throws TomConfigurationError;
    
    public boolean hasNext();
}
