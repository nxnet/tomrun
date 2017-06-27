package io.nxnet.tomrun.parser;

import io.nxnet.tomrun.model.TestProject;

public interface TestProjectIterator extends ExecutionElementIterator<TestProject>
{
    public TestProject next() throws TomConfigurationError;
    
    public boolean hasNext();
}
