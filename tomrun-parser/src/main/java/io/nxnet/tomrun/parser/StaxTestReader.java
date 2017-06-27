package io.nxnet.tomrun.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import io.nxnet.tomrun.model.Test;

public class StaxTestReader extends AbstractStaxReader implements TestReader
{
    
    public TestIterator read(InputStream in) throws TomConfigurationError
    {
        reader = createReader(new BufferedInputStream(in));
        return new InternalTestIterator(); 
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

    private class InternalTestIterator implements TestIterator {
        
        private Set<String> testIds = new HashSet<String>();
        
        public Test next() throws TomConfigurationError
        {
            try
            {
                Test test = processTest(reader, false);
                if (test != null && !testIds.add(test.getId())) 
                {
                    throw new TomConfigurationError(MessageFormat.format("Duplicate test ids ''{0}''", test.getId()));
                }
                return test;
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
                    if (event == XMLEvent.START_ELEMENT && "test".equals(reader.getLocalName()))
                    {
                        return true;
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
