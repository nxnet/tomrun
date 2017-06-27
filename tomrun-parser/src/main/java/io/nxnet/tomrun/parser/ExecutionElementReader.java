package io.nxnet.tomrun.parser;

import java.io.IOException;
import java.io.InputStream;

import io.nxnet.tomrun.model.WrapperAwareElement;

public interface ExecutionElementReader<E extends WrapperAwareElement>
{
    public ExecutionElementIterator<E> read(InputStream in) throws TomConfigurationError;
    
    public void close() throws IOException;
}
