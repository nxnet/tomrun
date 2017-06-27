package io.nxnet.tomrun.parser;

import java.io.IOException;
import java.io.InputStream;

import io.nxnet.tomrun.model.TestProject;

public interface TestProjectReader extends ExecutionElementReader<TestProject>
{
    public ExecutionElementIterator<TestProject> read(InputStream in) throws TomConfigurationError;
    
    public void close() throws IOException;
}
