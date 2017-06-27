package io.nxnet.tomrun.parser;

import io.nxnet.tomrun.model.TestCase;

public interface TestCaseIterator extends ExecutionElementIterator<TestCase>
{
    public TestCase next() throws TomConfigurationError;
    
    public boolean hasNext();
}
