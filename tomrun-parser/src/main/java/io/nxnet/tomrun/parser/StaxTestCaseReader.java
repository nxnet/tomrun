package io.nxnet.tomrun.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import io.nxnet.tomrun.model.TestCase;

public class StaxTestCaseReader extends AbstractStaxReader implements TestCaseReader
{
    
    public TestCaseIterator read(InputStream in) throws TomConfigurationError
    {
        reader = createReader(new BufferedInputStream(in));
        return new InternalTestCaseIterator(); 
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

    private class InternalTestCaseIterator implements TestCaseIterator {
        
        private Set<String> testCaseIds = new HashSet<String>();
        
        public TestCase next() throws TomConfigurationError
        {
            try
            {
                TestCase testCase = processTestCase(reader, false);
//                if (testCase != null && !testCaseIds.add(testCase.getId())) 
//                {
//                    throw new TomConfigurationError(MessageFormat.format("Duplicate test case ids ''{0}''", testCase.getId()));
//                }
                return testCase;
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
                    if (event == XMLEvent.START_ELEMENT && "test-case".equals(reader.getLocalName()))
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
