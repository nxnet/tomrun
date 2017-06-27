package io.nxnet.tomrun.parser;

import java.io.IOException;
import java.io.InputStream;

import io.nxnet.tomrun.model.TestCase;

public interface TestCaseReader extends ExecutionElementReader<TestCase>
{
    public TestCaseIterator read(InputStream in) throws TomConfigurationError;
    
    public void close() throws IOException;
}
