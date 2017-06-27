package io.nxnet.tomrun.parser;

import io.nxnet.tomrun.model.Test;

public interface TestIterator extends ExecutionElementIterator<Test>
{
    public Test next() throws TomConfigurationError;
    
    public boolean hasNext();
}
