package io.nxnet.tomrun.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import io.nxnet.tomrun.model.Wrapper;

public class StaxWrapperReader extends AbstractStaxReader implements WrapperReader
{
    
    public WrapperIterator read(InputStream in) throws TomConfigurationError
    {
        reader = createReader(new BufferedInputStream(in));
        return new InternalWrapperIterator(); 
    }

    public void close() throws IOException
    {
        try
        {
        	closeReader(reader);
        }
        finally
        {
        	reader = null;
        }
    }

    private class InternalWrapperIterator implements WrapperIterator {
        
        private Set<String> wrapperIds = new HashSet<String>();
        
        public Wrapper next() throws TomConfigurationError
        {
            try
            {
                Wrapper wrapper = processWrapper(reader, false);
                if (wrapper != null && !wrapperIds.add(wrapper.getId())) 
                {
                    throw new TomConfigurationError(MessageFormat.format("Duplicate wrappers with same id ''{0}''", wrapper.getId()));
                }
                return wrapper;
            }
            catch (XMLStreamException e)
            {
                throw new TomConfigurationError(e);
            }
            catch (IOException e)
            {
                throw new TomConfigurationError(e);
            }
        }

        public boolean hasNext()
        {
            try
            {
                while(reader.hasNext()) {
                    
                    int event = reader.next();
                    if (event == XMLEvent.START_ELEMENT)
                    {
	                    String localName = reader.getLocalName();
	                    if (localName == "before-first" || localName == "after-last"
	                    		|| localName == "before-each" || localName == "after-each")
	                    {
	                        return true;
	                    }
                    }
                }
                
                return false;
            }
            catch (XMLStreamException e)
            {
                throw new RuntimeException(e);
            }
        }

    }

}
