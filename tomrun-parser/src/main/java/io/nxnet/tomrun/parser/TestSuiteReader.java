package io.nxnet.tomrun.parser;

import java.io.IOException;
import java.io.InputStream;

import io.nxnet.tomrun.model.TestSuite;

public interface TestSuiteReader extends ExecutionElementReader<TestSuite>
{
    public ExecutionElementIterator<TestSuite> read(InputStream in) throws TomConfigurationError;
    
    public void close() throws IOException;
}
