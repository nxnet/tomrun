package io.nxnet.tomrun.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import io.nxnet.tomrun.model.TestProject;

public class StaxTestProjectReader extends AbstractStaxReader implements TestProjectReader 
{

    public TestProjectIterator read(InputStream in) throws TomConfigurationError
    {
        if (in == null)
        {
            throw new IllegalArgumentException("Input stream can't be null");
        }
        
        reader = createReader(new BufferedInputStream(in));
        return new InternalTestProjectIterator();
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

    private class InternalTestProjectIterator implements TestProjectIterator {
        
        private Set<String> testProjectIds = new HashSet<String>();
        
        public TestProject next() throws TomConfigurationError
        {
            try
            {
                TestProject testProject = processTestProject(reader);
                if (testProject != null && !testProjectIds.add(testProject.getId())) 
                {
                    throw new TomConfigurationError(MessageFormat.format("Duplicate test project ids ''{0}''", testProject.getId()));
                }
                return testProject;
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
                    if (event == XMLEvent.START_ELEMENT && "test-project".equals(reader.getLocalName()))
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
