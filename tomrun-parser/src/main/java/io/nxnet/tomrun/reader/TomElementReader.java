package io.nxnet.tomrun.reader;

import org.apache.commons.io.input.XmlStreamReader;

import io.nxnet.tomrun.model.TomElement;
import io.nxnet.tomrun.parser.TomConfigurationError;

public interface TomElementReader<E extends TomElement>
{
    public E read(XmlStreamReader reader) throws TomConfigurationError;
    
}
