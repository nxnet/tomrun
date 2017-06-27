package io.nxnet.tomrun.parser;

import java.io.IOException;
import java.io.InputStream;

import io.nxnet.tomrun.model.Wrapper;

public interface WrapperReader extends ExecutionElementReader<Wrapper>
{
    public WrapperIterator read(InputStream in) throws TomConfigurationError;
    
    public void close() throws IOException;
}
