package io.nxnet.tomrun.parser;

import java.io.IOException;
import java.io.InputStream;

import io.nxnet.tomrun.model.Test;

public interface TestReader extends ExecutionElementReader<Test>
{
    public TestIterator read(InputStream in) throws TomConfigurationError;
    
    public void close() throws IOException;
}
