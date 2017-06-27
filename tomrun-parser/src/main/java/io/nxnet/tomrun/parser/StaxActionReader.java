package io.nxnet.tomrun.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import io.nxnet.tomrun.model.AbstractAction;

public class StaxActionReader extends AbstractStaxReader implements ActionReader
{
    
    public ActionIterator read(InputStream in) throws TomConfigurationError
    {
        reader = createReader(new BufferedInputStream(in));
        return new InternalActionIterator(); 
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

    private class InternalActionIterator implements ActionIterator {
        
        private Set<String> actionIds = new HashSet<String>();
        
        public AbstractAction next() throws TomConfigurationError
        {
            try
            {
                AbstractAction test = processAction(reader, false);
                if (test != null && !actionIds.add(test.getId())) 
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
                    if (event == XMLEvent.START_ELEMENT && "script".equals(reader.getLocalName()))
                    {
                        return true;
                    }
                    if (event == XMLEvent.START_ELEMENT && "http-request".equals(reader.getLocalName()))
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
