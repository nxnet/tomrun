package io.nxnet.tomrun.parser;

import java.io.IOException;
import java.io.InputStream;

import io.nxnet.tomrun.model.AbstractAction;

public interface ActionReader extends ExecutionElementReader<AbstractAction>
{
    public ActionIterator read(InputStream in) throws TomConfigurationError;
    
    public void close() throws IOException;
}
