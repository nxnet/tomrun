package io.nxnet.tomrun.parser;

import io.nxnet.tomrun.model.TestSuite;

public interface TestSuiteIterator extends ExecutionElementIterator<TestSuite>
{
    public TestSuite next() throws TomConfigurationError;
    
    public boolean hasNext();
}
